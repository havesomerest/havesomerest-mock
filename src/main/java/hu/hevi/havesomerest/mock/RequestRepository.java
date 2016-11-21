package hu.hevi.havesomerest.mock;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class RequestRepository {

    private List<AcceptedRequest> requests = new ArrayList<>();

    public void save(AcceptedRequest acceptedRequest) {
        requests.add(acceptedRequest);
    }

    public AcceptedRequest get(int i) {
        return requests.get(i);
    }

    public int size() {
        return requests.size();
    }

    public Stream<AcceptedRequest> stream() {
        return requests.stream();
    }
}
