package com.example.datadynamo;

import com.example.datadynamo.ClientIntake;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ClientIntakeRepository extends MongoRepository<ClientIntake, String> {
    List<ClientIntake> findByService(String service);
    List<ClientIntake> findByStaff(String staff); // Filter by staff only
    List<ClientIntake> findByServiceAndStaff(String service, String staff); // Filter by both service and staff

    @Query("{ $and: [ "
            + "{ $or: [ "
            + "  { 'lastName': { $regex: ?0, $options: 'i' } }, "
            + "  { 'firstName': { $regex: ?0, $options: 'i' } }, "
            + "  { 'referenceNumber': { $regex: ?0, $options: 'i' } }, "
            + "  { 'service': { $regex: ?0, $options: 'i' } }, "
            + "  { 'email': { $regex: ?0, $options: 'i' } } ,"
            + "  { 'region': { $regex: ?0, $options: 'i' } } "
            + "] }, "
            + "{ 'staff': ?1 } ] }")
    List<ClientIntake> keywordSearch(String searchText, String staff);


    @Query(value = "{'staff': ?0}", sort = "{ 'startDate': -1 }")
    List<ClientIntake> findByStaffSortedByStartDate(String staff);


    @Query("{"
            + "?0 != null ? { 'region' : ?0 } : {}"
            + "?1 != null ? { 'staff' : ?1 } : {}"
            + "?2 != null ? { 'service' : ?2 } : {}"
            + "?3 != null ? { 'startDate' : { '$gte' : ?3, '$lte' : ?4 } } : {}"
            + "}")
    List<ClientIntake> findByFilters(String region, String staff, String service, Date startDateFilter, Date endDateFilter);
}
