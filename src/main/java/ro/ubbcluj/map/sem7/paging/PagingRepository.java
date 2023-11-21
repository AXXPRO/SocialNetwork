package ro.ubbcluj.map.sem7.paging;


import ro.ubbcluj.map.sem7.domain.Entity;
import ro.ubbcluj.map.sem7.repository.Repository;

public interface PagingRepository<ID,
        E extends Entity<ID>>
        extends Repository<ID, E> {

    Page<E> findAll(Pageable pageable);
}
