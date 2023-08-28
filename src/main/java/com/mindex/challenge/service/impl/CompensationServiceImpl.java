package com.mindex.challenge.service.impl;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import org.springframework.stereotype.Service;


import java.util.UUID;
@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private CompensationRepository compensationRepository;


    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation: [{}]", compensation);
        Employee existingEmployee= employeeRepository.findByEmployeeId(compensation.getEmployee().getEmployeeId());
        Compensation existingCompensation= compensationRepository.findByEmployee(existingEmployee);

      if(compensation.getEmployee().getEmployeeId()!=null && existingEmployee!=null){
          if(existingCompensation==null){
              compensation.setEmployee(existingEmployee);
              compensationRepository.insert(compensation);
          }
          else{
              throw new RuntimeException("You already created  compensation for  Employee with id " + existingEmployee.getEmployeeId());
          }
      }
      else
      {
          employeeService.create(compensation.getEmployee());
          compensationRepository.insert(compensation);
      }
        return compensation;
    }


    @Override
    public Compensation read(String id) {
        LOG.debug("Creating Compensation with id [{}]", id);
        Employee employee = employeeService.read(id);
        Compensation compensation = compensationRepository.findByEmployee(employee);

        if (compensation == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return compensation;
    }
}
