package ro.ubbcluj.map.sem7.paging;
import java.util.stream.Stream;

public interface Page<E> {
    Pageable getPageable();

    Pageable nextPageable();

    Stream<E> getContent();


    Pageable previousPageable();
}
