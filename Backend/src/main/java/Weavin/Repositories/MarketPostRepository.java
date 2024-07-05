package Weavin.Repositories;

import Weavin.Entities.MarketPost;
import Weavin.Entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MarketPostRepository extends CrudRepository<MarketPost, Integer> {
    List<MarketPost> findByUserAndReportStatusFalse(User user);
    List<MarketPost> findByReportStatusFalse();
}
