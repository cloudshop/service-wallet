package com.eyun.wallet.repository;

import com.eyun.wallet.domain.TicketDetails;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the TicketDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketDetailsRepository extends JpaRepository<TicketDetails, Long>, JpaSpecificationExecutor<TicketDetails> {

}
