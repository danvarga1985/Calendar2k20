package com.calendar.service.impl;

import com.calendar.dao.EntryDao;
import com.calendar.data.enums.EntryPhase;
import com.calendar.data.enums.EntryType;
import com.calendar.domain.Entry;
import com.calendar.domain.User;
import com.calendar.exceptions.ConstraintViolationException;
import com.calendar.exceptions.EntryNotFoundException;
import com.calendar.exceptions.InvalidEntryTypeException;
import com.calendar.repository.EntryRepository;
import com.calendar.repository.custom.CustomEntryRepository;
import com.calendar.requestdto.EntryDto;
import com.calendar.requestdto.EntryForModificationDto;
import com.calendar.requestdto.ProjectDto;
import com.calendar.requestdto.SingleDto;
import com.calendar.responsedto.*;
import com.calendar.service.EntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final CustomEntryRepository customEntryRepository;
    private final UserServiceImpl userServiceImpl;
    private final EntryDao entryDao;

    @Autowired
    public EntryServiceImpl(EntryRepository entryRepository, UserServiceImpl userServiceImpl,
                            CustomEntryRepository customEntryRepository, EntryDao entryDao, EntityManager em) {
        this.entryRepository = entryRepository;
        this.userServiceImpl = userServiceImpl;
        this.customEntryRepository = customEntryRepository;
        this.entryDao = entryDao;
    }

    @Override
    @Transactional
    public ProjectViewResponseDto createProject(ProjectDto projectDto) {
        User user = getCurrentUser();

        Entry entry = new Entry(projectDto.getTitle(), projectDto.getDescription(), null, null,
                projectDto.getDeadline(),
                EntryType.PROJECT, EntryPhase.NONRELEVANT);

        entry.setUserId(user.getId());

        int numberOfProjects = customEntryRepository.getActiveOrClosedProjects(user.getId(), false).size();
        entry.setSortNumber(numberOfProjects);

        entryRepository.save(entry);

        //Need to create the response object here in order to avoid the recursive query() --> getProjectIdOfEntry
        return new ProjectViewResponseDto(getFullProjectById(entry.getId()), getProjects());
    }

    @Override
    @Transactional
    public ProjectViewResponseDto createEntry(EntryDto entryDto) {
        User user = getCurrentUser();

        EntryType entryType;
        EntryPhase entryPhase;

        if (entryDto.getEntryType() != null) {
            entryType = EntryType.valueOf(entryDto.getEntryType());
        } else {
            entryType = EntryType.TASK;
        }
        if (entryDto.getEntryPhase() != null) {
            entryPhase = EntryPhase.valueOf(entryDto.getEntryPhase());
        } else {
            entryPhase = EntryPhase.NONRELEVANT;
        }

        Entry entry = new Entry(
                entryDto.getTitle(),
                entryDto.getDescription(),
                entryDto.getDate(),
                entryDto.getDuration(),
                entryDto.getDeadline(),
                entryType,
                entryPhase);

        entry.setUserId(user.getId());
        entry.setParentEntry(entryRepository.getOne(entryDto.getParentId()));
        entry.setChild(true);

        int numberOfEntriesOnThisLevel = entryRepository.findById(entryDto.getParentId())
                .get().getChildEntries().size();
        entry.setSortNumber(numberOfEntriesOnThisLevel);

        entryRepository.save(entry);

		/* Need to manually add the recently created entry to it's parent's children, otherwise it would not be
		visible in the response. --> Recursive query
		*/
        Entry parentOfEntry = entryRepository.findById(entry.getParentEntry().getId()).get();
        parentOfEntry.getChildEntries().add(entry);

        return getProjectView(parentOfEntry.getId());
    }

    @Override
    @Transactional
    public SingleDto createSingle(SingleDto singleDto) {
        User user = getCurrentUser();

        EntryType entryType;
        EntryPhase entryPhase;

        if (singleDto.getEntryType() != null) {
            if (singleDto.getEntryType().equals(EntryType.TASK.toString()) ||
                    singleDto.getEntryType().equals(EntryType.EVENT.toString()) ||
                    singleDto.getEntryType().equals(EntryType.MEMO.toString())) {
                entryType = EntryType.valueOf(singleDto.getEntryType());
            } else {
                throw new InvalidEntryTypeException("EntryType can only be Task, Memo or Event!");
            }
        } else {
            entryType = EntryType.TASK;
        }

        if (singleDto.getEntryPhase() != null) {
            entryPhase = EntryPhase.valueOf(singleDto.getEntryPhase());
        } else {
            entryPhase = EntryPhase.NONRELEVANT;
        }

        Entry single = new Entry(
                singleDto.getTitle(),
                singleDto.getDescription(),
                singleDto.getDate(),
                singleDto.getDuration(),
                singleDto.getDeadline(),
                entryType,
                entryPhase);

        single.setUserId(user.getId());
        Entry savedEntry = entryRepository.save(single);

        return new SingleDto(savedEntry.getTitle(), savedEntry.getDescription(), savedEntry.getDate(),
                savedEntry.getDuration(), savedEntry.getDeadline(), savedEntry.getEntryType().toString(),
                savedEntry.getEntryPhase().toString());
    }

    @Override
    public EntryListResponseDto getEntries() {
        EntryListResponseDto entryResponseDto = new EntryListResponseDto();

        User user = getCurrentUser();
        List<Entry> entryList = new ArrayList<Entry>();
        entryList = customEntryRepository.getOrderedEntriesByUserId(user.getId());
        entryResponseDto.setEntryList(entryList);

        return entryResponseDto;
    }

    @Override
    public ArrayList<ProjectEntriesResponseDto> getProjects() {
        User user = getCurrentUser();
        List<Entry> entryList = new ArrayList<Entry>();

        if (user.isOnlyActiveProjects()) {
            entryList = customEntryRepository.getActiveOrClosedProjects(user.getId(), false);
        } else {
            entryList = customEntryRepository.getAllProjects(user.getId());
        }

        ArrayList<ProjectEntriesResponseDto> perDtoList = new ArrayList<ProjectEntriesResponseDto>();
        for (int i = 0; i < entryList.size(); i++) {
            Entry entry = entryList.get(i);
            ProjectEntriesResponseDto perDto = new ProjectEntriesResponseDto(entry.getId(), entry.getTitle(),
                    entry.isClosed(), entry.getSortNumber());
            perDtoList.add(perDto);
        }

        return perDtoList;
    }

    @Override
    public FullProjectResponseDto getFullProjectById(int id) {
        Optional<Entry> entry = entryRepository.findById(id);
        Entry e = entry.get();

        checkUserToEntry(e);

        return new FullProjectResponseDto(e.getId(), e.getUserId(), e.getTitle(), e.getDescription(), e.getDate(),
                e.getDuration(), e.getDeadline(), e.getEntryType(), e.getEntryPhase(), e.isChild(), e.isClosed(),
                e.isDeleted(), e.getSortNumber(), e.isExpanded(), e.getChildEntries());
    }

    @Override
    public ProjectViewResponseDto getProjectView(Integer id) {
        User user = getCurrentUser();

        if (id != null) {
            return new ProjectViewResponseDto(getFullProjectById(getProjectIdOfEntry(id)),
                    getProjects());
        } else {
            return new ProjectViewResponseDto(null, getProjects());
        }
    }


    public ProjectViewResponseForModificationDto getProjectViewWithChildrenClosedStatus(Integer id,
                                                                                        boolean hasOpenChildren,
                                                                                        Boolean allSiblingsAreClosed) {
        User user = getCurrentUser();

        if (id != null) {
            if (hasOpenChildren) {
                return new ProjectViewResponseForModificationDto(null, null,
                        true, allSiblingsAreClosed);
            } else {
                return new ProjectViewResponseForModificationDto(getFullProjectById(getProjectIdOfEntry(id)),
                        getProjects(), null, allSiblingsAreClosed);
            }
        } else {
            return new ProjectViewResponseForModificationDto(null,
                    getProjects(), null, allSiblingsAreClosed);
        }

    }

    @Override
    public EntryResponseDto getEntryById(int id) {
        Optional<Entry> entry = entryRepository.findById(id);
        Entry e = entry.get();

        EntryResponseDto erDto;

        try {
            erDto = new EntryResponseDto(e.getId(), e.getUserId(), e.getTitle(), e.getDescription(),
                    e.getDate(), e.getDuration(), e.getDeadline(), e.getEntryType(), e.getEntryPhase(), e.isChild(),
                    e.isClosed(), e.getSortNumber(), e.isDeleted(), e.isExpanded(), e.getParentEntry().getId());
        } catch (NullPointerException ex) {
            erDto = new EntryResponseDto(e.getId(), e.getUserId(), e.getTitle(), e.getDescription(),
                    e.getDate(), e.getDuration(), e.getDeadline(), e.getEntryType(), e.getEntryPhase(), e.isChild(),
                    e.isClosed(), e.getSortNumber(), e.isDeleted(), e.isExpanded(), null);
        }

        User user = getCurrentUser();
        if (user.getId() != erDto.getUserId()) {
            throw new AccessDeniedException("Access denied");
        }

        return erDto;
    }

    @Override
    @Transactional
    public ProjectViewResponseDto deleteEntryById(int id) {
        Optional<Entry> entry = entryRepository.findById(id);
        Entry e = entry.get();

        checkUserToEntry(e);

        customEntryRepository.removeEntry(e);

        try {
            int parentId = e.getParentEntry().getId();
            return getProjectView(parentId);

        } catch (NullPointerException exception) {
            return getProjectView(null);
        }

    }

    @Transactional
    public ProjectViewResponseForModificationDto modifyEntry(Entry entry, EntryForModificationDto eDto,
                                                             boolean checkIfAllChildrenAreClosed,
                                                             Boolean areAllSiblingsClosed) {
        if (eDto.getTitle() != null) {
            if (eDto.getTitle().equals(Optional.empty())) {
                throw new ConstraintViolationException("Title can't be null!");
            } else {
                entry.setTitle(eDto.getTitle().get());
            }
        }

        if (eDto.getDescription() != null) {
            if (eDto.getDescription().equals(Optional.empty())) {
                entry.setDescription(null);
            } else {
                entry.setDescription(eDto.getDescription().get());
            }
        }

        if (eDto.getDate() != null) {
            if (eDto.getDate().equals(Optional.empty())) {
                entry.setDate(null);
            } else {
                entry.setDate(eDto.getDate().orElse(entry.getDate()));
            }
        }

        if (eDto.getDuration() != null) {
            if (eDto.getDuration().equals(Optional.empty())) {
                entry.setDuration(null);
            } else {
                entry.setDuration(eDto.getDuration().get());
            }

        }

        if (eDto.getDeadline() != null) {
            if (eDto.getDeadline().equals(Optional.empty())) {
                entry.setDeadline(null);
            } else {
                entry.setDeadline(eDto.getDeadline().get());
            }
        }

        if (eDto.getEntryPhase() != null) {
            entry.setEntryPhase(EntryPhase.valueOf(eDto.getEntryPhase().get()));
        }

        if (eDto.getEntryType() != null) {
            entry.setEntryType(EntryType.valueOf(eDto.getEntryType().get()));
        }

        if (eDto.isClosed() != null) {
            entry.setClosed(eDto.isClosed().get());
        }

        if (eDto.isExpanded() != null) {
            entry.setExpanded(eDto.isExpanded().get());
        }

        entryRepository.save(entry);

        return getProjectViewWithChildrenClosedStatus(entry.getId(), checkIfAllChildrenAreClosed, areAllSiblingsClosed);
    }

    @Override
    public ProjectViewResponseForModificationDto modifyEntryById(int id, EntryForModificationDto eDto,
                                                                 boolean checkIfAllChildrenAreClosed,
                                                                 boolean checkIfAllSiblingsAreClosed) {
        Entry entry = entryRepository.findById(id).get();

        checkUserToEntry(entry);

        Boolean areAllSiblingsClosed = null;

        if (checkIfAllSiblingsAreClosed) {
            areAllSiblingsClosed = areAllSiblingEntriesClosed(entry);
        }

        if (!checkIfAllChildrenAreClosed) {
            return modifyEntry(entry, eDto, false, areAllSiblingsClosed);
        } else {
            if (hasOpenChildren(id)) {
                return getProjectViewWithChildrenClosedStatus(id, true, areAllSiblingsClosed);
            } else {
                return modifyEntry(entry, eDto, false, areAllSiblingsClosed);
            }
        }
    }

    @Override
    @Transactional
    public ProjectViewResponseDto modifyProjectById(int id, ProjectDto projectDto) {
        Entry project = entryRepository.findById(id).get();

        checkUserToEntry(project);

        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());

        entryRepository.save(project);

        return getProjectView(id);
    }

    @Transactional
    @Override
    public void expandEntry(int id, boolean isExpanded) {
        Entry entry;

        try {
            entry = entryRepository.findById(id).get();
        } catch (NoSuchElementException e) {
            throw new EntryNotFoundException("Entry doesn't exist!");
        }

        checkUserToEntry(entry);

        entry.setExpanded(isExpanded);

        entryRepository.save(entry);
    }

    @Override
    public List<SortedEntryDto> getSortedEntries(LocalDateTime startDate, LocalDateTime endDate) {
        int userId = userServiceImpl.getFullUser().getId();

        List<Entry> sortedEntries = customEntryRepository.getSortedEntries(userId, startDate, endDate);

        List<SortedEntryDto> resultList = new ArrayList<>();

        for (Entry e : sortedEntries) {
            resultList.add(convertEntryToSortedEntryDto(e));
        }

        return resultList;
    }

    private int getProjectIdOfEntry(int entryId) {
        Entry entry = entryRepository.findById(entryId).get();

        checkUserToEntry(entry);

        int parentId = entryDao.getProjectIdOfEntry(entryId);

        return parentId;
    }

    private boolean hasOpenChildren(int id) {
        Entry entry = entryRepository.findById(id).get();

        return entry.getChildEntries().stream().anyMatch(child -> !child.isClosed());
    }

    private boolean areAllSiblingEntriesClosed(Entry aEntry) {
        try {
            return aEntry.getParentEntry().getChildEntries().stream()
                    .noneMatch(child -> !child.isClosed() && child.getId() != aEntry.getId());
        } catch (NullPointerException e) {
            return false;
        }
    }

    private SortedEntryDto convertEntryToSortedEntryDto(Entry entry) {
        String parentTitle;
        try {
            parentTitle = entryRepository.getOne(entry.getParentEntry().getId()).getTitle();
        } catch (NullPointerException e) {
            parentTitle = null;
        }

        return new SortedEntryDto(entry.getId(), entry.getTitle(), parentTitle, entry.getDate(),
                entry.getDuration(), entry.getDeadline(), entry.getEntryType(), entry.getEntryPhase(), entry.isClosed());
    }

    private User getCurrentUser() {
        return userServiceImpl.getFullUser();
    }

    private void checkUserToEntry(Entry e) {
        User user = getCurrentUser();
        if (user.getId() != e.getUserId()) {
            throw new AccessDeniedException("Access denied");
        }
    }

}
