package hu.hevi.havesomerest.mock;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

@Repository
public class RequestRepository {

    private LinkedList<AcceptedRequest> requests = new LinkedList<>();

    public void save(AcceptedRequest acceptedRequest) {
        requests.add(0, acceptedRequest);
    }

    public AcceptedRequest get(int i) {
        return requests.get(i);
    }

    public List<AcceptedRequest> getRequests(boolean withBody) {
        List<AcceptedRequest> returnValue = new ArrayList<>();
        requests.forEach(request -> {
            if (withBody && request.getRequestBody() != null && !"".equals(request.getRequestBody().trim())) {
                returnValue.add(request);
            }
        });
        return returnValue;
    }

    public int size() {
        return requests.size();
    }

    public Stream<AcceptedRequest> stream() {
        return requests.stream();
    }
}
