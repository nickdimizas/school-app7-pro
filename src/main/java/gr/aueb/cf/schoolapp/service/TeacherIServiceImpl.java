package gr.aueb.cf.schoolapp.service;

import gr.aueb.cf.schoolapp.dao.ITeacherDAO;
import gr.aueb.cf.schoolapp.dao.exceptions.TeacherDAOException;
import gr.aueb.cf.schoolapp.dto.TeacherInsertDTO;
import gr.aueb.cf.schoolapp.dto.TeacherReadOnlyDTO;
import gr.aueb.cf.schoolapp.dto.TeacherUpdateDTO;
import gr.aueb.cf.schoolapp.exceptions.TeacherAlreadyExistsException;
import gr.aueb.cf.schoolapp.exceptions.TeacherNotFoundException;
import gr.aueb.cf.schoolapp.mapper.Mapper;
import gr.aueb.cf.schoolapp.model.Teacher;

import java.util.List;

public class TeacherIServiceImpl implements ITeacherService {

    private final ITeacherDAO teacherDAO;

    // Constructor Injection
    public TeacherIServiceImpl(ITeacherDAO teacherDAO) {
        this.teacherDAO = teacherDAO;
    }

    @Override
    public TeacherReadOnlyDTO insertTeacher(TeacherInsertDTO dto)
            throws TeacherDAOException, TeacherAlreadyExistsException {
        Teacher teacher;
        Teacher insertedTeacher;

        try {
            teacher = Mapper.mapTeacherInsertToModel(dto);
            if (teacherDAO.getTeacherByVat(dto.getVat()) != null)
                throw new TeacherAlreadyExistsException("Teacher with vat" + dto.getVat() + " exists");
            insertedTeacher = teacherDAO.insert(teacher);
            // logging
            return Mapper.mapTeacherToReadOnlyDTO(insertedTeacher).orElse(null);
        } catch (TeacherDAOException e) {
            //e.printStackTrace();
            // logging
            // rollback
            throw e;
        } catch (TeacherAlreadyExistsException e) {
            //e.printStackTrace();
            // logging
            // rollback
            throw e;
        }
    }

    @Override
    public TeacherReadOnlyDTO updateTeacher(Integer id, TeacherUpdateDTO dto)
            throws TeacherNotFoundException, TeacherAlreadyExistsException, TeacherDAOException {
        Teacher teacher;
        Teacher fetchedTeacher;

        try {
            if (teacherDAO.getById(id) == null)
                throw new TeacherNotFoundException("Teacher with id " + id + " was not found");


            fetchedTeacher = teacherDAO.getTeacherByVat(dto.getVat());
            if (fetchedTeacher != null && !fetchedTeacher.getId().equals(dto.getId())) {
                throw new TeacherAlreadyExistsException("Teacher with id: " + dto.getId() + " already exists");
            }
            teacher = Mapper.mapTeacherUpdateToModel(dto);
            Teacher updatedTeacher =  teacherDAO.update(teacher);
            // logging
            return Mapper.mapTeacherToReadOnlyDTO(updatedTeacher).orElse(null);
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            //e.printStackTrace();
            // logging
            // rollback
            throw e;
        }
    }

    @Override
    public void deleteTeacher(Integer id)
            throws TeacherDAOException, TeacherNotFoundException {

        try {
            if (teacherDAO.getById(id) == null) {
                throw new TeacherNotFoundException("Teacher not found");
            }
            teacherDAO.delete(id);
        } catch (TeacherDAOException | TeacherNotFoundException e) {
            // e.printStackTrace();
            // rollback
            throw e;
        }
    }

    @Override
    public TeacherReadOnlyDTO getTeacherById(Integer id) throws TeacherDAOException, TeacherNotFoundException {
        return null;
    }

    @Override
    public List<TeacherReadOnlyDTO> getAllTeachers() throws TeacherDAOException {
        return List.of();
    }

    @Override
    public List<TeacherReadOnlyDTO> getTeachersByLastname(String lastname) throws TeacherDAOException {
        return List.of();
    }
}
