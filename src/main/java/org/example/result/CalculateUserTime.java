package org.example.result;

import org.example.controller.Get;
import org.example.event.Event;

import java.io.IOException;
import java.util.*;

public class CalculateUserTime {

    /**
     * Calculates and retrieves the total time spent by each user on various workloads.
     *
     * @return ResultData object encapsulating the calculated results.
     * @throws RuntimeException If IllegalAccessException occurs during the process.
     */
    public ResultData calculateUserUsedTime() throws RuntimeException {
        // Maps to store start and stop timestamps for each workload
        Map<String, Long> mapEventStart = new HashMap<>();
        Map<String, Long> mapEventStop = new HashMap<>();

        try {
            // Retrieve timestamps for start and stop events using the new GetService class
            List<Event> events = new Get().getServiceResponse();
            for (Event event : events) {
                String workloadId = event.getWorkloadId();

                if ("start".equals(event.getEventType())) {
                    mapEventStart.put(workloadId, event.getTimestamp());
                } else if ("stop".equals(event.getEventType())) {
                    mapEventStop.put(workloadId, event.getTimestamp());
                }
            }

            // Maps to store total workload time and map workloadId to userId
            Map<String, Long> totalWorkload = new HashMap<>();
            Map<String, String> mapWorkId_mit_UserId = new HashMap<>();

            // Calculate total workload for each workloadId
            for (Event event : events) {
                String workloadId = event.getWorkloadId();
                Long startTime = mapEventStart.get(workloadId);
                Long stopTime = mapEventStop.get(workloadId);

                if (startTime != null && stopTime != null) {
                    long timeSpent = stopTime - startTime;
                    totalWorkload.put(workloadId, timeSpent);
                    mapWorkId_mit_UserId.put(workloadId, event.getCustomerId());
                }
            }

            // Map to store the total consumption time per user
            Map<String, Long> saveResultMap = new HashMap<>();

            // Sum up total consumption time per user
            for (Map.Entry<String, Long> entry : totalWorkload.entrySet()) {
                String workloadId = entry.getKey();
                Long timeSpent = entry.getValue();
                String userId = mapWorkId_mit_UserId.get(workloadId);

                if (userId != null) {
                    saveResultMap.put(userId, saveResultMap.getOrDefault(userId, 0L) + timeSpent);
                }
            }

            // Convert the result to the desired format
            List<Result> results = new ArrayList<>();
            for (Map.Entry<String, Long> entry : saveResultMap.entrySet()) {
                results.add(new Result(entry.getKey(), entry.getValue()));
            }

            return new ResultData(results);

        } catch (IOException e) {
            // Wrap and rethrow the exception as a RuntimeException
            throw new RuntimeException(e.getMessage());
        }
    }
}
