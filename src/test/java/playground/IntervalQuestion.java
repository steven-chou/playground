package playground;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class IntervalQuestion {
    /**
     * Merge Intervals
     * Given an array of intervals where intervals[i] = [starti, endi], merge all overlapping intervals,
     * and return an array of the non-overlapping intervals that cover all the intervals in the input.
     * <p>
     * Input: intervals = [[1,3],[2,6],[8,10],[15,18]]
     * Output: [[1,6],[8,10],[15,18]]
     * Explanation: Since intervals [1,3] and [2,6] overlap, merge them into [1,6].
     * <p>
     * Input: intervals = [[1,4],[4,5]]
     * Output: [[1,5]]
     * Explanation: Intervals [1,4] and [4,5] are considered overlapping.
     * <p>
     * https://leetcode.com/problems/merge-intervals/description/
     */
    @Test
    void testMergeIntervals() {
        int[][] input = new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] answer = {
                {1, 6}, {8, 10}, {15, 18}
        };
        int[][] result = merge(input);
        Assertions.assertThat(result).isDeepEqualTo(answer);
    }

    /**
     * Sort the interval array first, then insert the 1st interval into the merged result and then iterate
     * the intervals and compare each one with the last interval in the merged result and merge them if needed.
     * <p>
     * Observation:
     * 1. The required non-overlapping output is sorted by the start time, and we also need to compare start
     * and end time of any two intervals to decide to merge, so sorting the input will be beneficial.
     * 2. Once the array is sorted, we can iterate and compare two intervals at one time and determine if
     * they need to be merged.
     * <p>
     * Algo:
     * First, we sort the list. Then, we insert the first interval into our merged list and
     * continue considering each interval in turn as follows:
     * <p>
     * If the current interval begins before the previous interval ends(The last interval in the merged result),
     * then they do overlap, and we merge them by updating the end of the previous interval if it is less than
     * the end of the current interval.
     * <p>
     * Otherwise, they do not overlap and we can append the current interval to merged result.
     * <p>
     * Time complexity: O(n⋅log n), dominated by the sorting
     * Space complexity: O(n)
     */
    int[][] merge(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        // or Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> result = new ArrayList<>(); // Add 1st interval to result first so we can start comparison from the 2nd interval
        result.add(intervals[0]);
        for (int i = 1; i < intervals.length; i++) { // Start from the 2nd interval
            int[] lastMergedInterval = result.get(result.size() - 1);
            int lastMergedIntervalEnd = result.get(result.size() - 1)[1];
            int currentStart = intervals[i][0];
            if (currentStart <= lastMergedIntervalEnd)
                // Current interval is overlapped w/ the last interval in the merged result, so we need to merge,
                // which means updating the end time of the last interval to the greater end time of these two.
                lastMergedInterval[1] = Math.max(lastMergedIntervalEnd, intervals[i][1]);
            else
                // No overlap, so just add it to the result
                result.add(intervals[i]);
        }
        return result.toArray(new int[0][0]);
    }

    /**
     * Interval List Intersections
     * You are given two lists of closed intervals, firstList and secondList, where firstList[i]
     * = [starti, endi] and secondList[j] = [startj, endj]. Each list of intervals is pairwise disjoint
     * and in sorted order.
     * <p>
     * Return the intersection of these two interval lists.
     * <p>
     * A closed interval [a, b] (with a <= b) denotes the set of real numbers x with a <= x <= b.
     * <p>
     * The intersection of two closed intervals is a set of real numbers that are either empty or represented
     * as a closed interval. For example, the intersection of [1, 3] and [2, 4] is [2, 3].
     * <p>
     * Input: firstList = [[0,2],[5,10],[13,23],[24,25]], secondList = [[1,5],[8,12],[15,24],[25,26]]
     * Output: [[1,2],[5,5],[8,10],[15,23],[24,24],[25,25]]
     * <p>
     * Input: firstList = [[1,3],[5,9]], secondList = []
     * Output: []
     * <p>
     * https://leetcode.com/problems/interval-list-intersections/description/
     */
    @Test
    void testIntervalIntersection() {
        int[][] firstList = new int[][]{{0, 2}, {5, 10}, {13, 23}, {24, 25}};
        int[][] secondList = new int[][]{{1, 5}, {8, 12}, {15, 24}, {25, 26}};
        int[][] answer = new int[][]{{1, 2}, {5, 5}, {8, 10}, {15, 23}, {24, 24}, {25, 25}};
        Assertions.assertThat(intervalIntersection(firstList, secondList)).isDeepEqualTo(answer);
    }

    /**
     * Iterate two list with two pointers until we exhaust one of lists, for each interval on both
     * lists, get the startTimeMax and endTimeMin from these two. If endTimeMin >= startTimeMax,
     * it means there is overlap, so add it to the result. Then if endTimeMin == firstPtr interval
     * endTime, advance firstPtr, if endTimeMin == secondPtr interval endTime, advance secondPtr.
     * <p>
     * Observation:
     * 1. To detect two overlapped intervals, we check a.endTime > b.startTime if a.startTime <=
     * b.startTime. However, if we don't want to check the startTime of both a and b, this can also
     * be simplified as first take the max startTime and the min endTime of these two intervals.
     * If startTimeMax <= endTimeMin, this will be exactly the overlapped interval section. So
     * we will add it to the answer.
     * <p>
     * 2. The next important thing is we need to decide when to move the two pointers. The interval
     * which has the ennTime equal to endMin means it can't be intersected with the next interval
     * on the other list, cuz the other interval we are comparing with now must have endTime equal
     * or greater than the endMin, so we advance the pointer on this interval list.
     * <p>
     * Time Complexity: O(M+N), where M, N are the lengths of two lists respectively
     * Space Complexity: O(M+N), the maximum size of the answer.
     */
    public int[][] intervalIntersection(int[][] firstList, int[][] secondList) {
        int firstPtr = 0, secondPtr = 0;
        List<int[]> result = new ArrayList<>();
        while (firstPtr < firstList.length && secondPtr < secondList.length) {
            // Determine the startMax and endMin between two intervals
            int startMax = Math.max(firstList[firstPtr][0], secondList[secondPtr][0]);
            int endMin = Math.min(firstList[firstPtr][1], secondList[secondPtr][1]);

            // When two intervals are NOT overlapped, startMax must be greater than endMin
            if (endMin >= startMax) {
                result.add(new int[]{startMax, endMin});
            }
            // Now we need to decide to move the two pointers. The interval which has the ennTime equal to endMin means
            // it can't be intersected with the next interval on the other list, cuz the other interval we are comparing
            // with now must have endTime equal or greater than the endMin, so we advance the pointer on this interval
            // list
            if (endMin == firstList[firstPtr][1])
                firstPtr++;
            if (endMin == secondList[secondPtr][1])
                secondPtr++;
        }
        return result.toArray(new int[0][0]);
    }

    /**
     * Insert Interval
     * You are given an array of non-overlapping intervals intervals where intervals[i] = [starti, endi]
     * represent the start and the end of the ith interval and intervals is sorted in ascending order by starti.
     * You are also given an interval newInterval = [start, end] that represents the start and end of
     * another interval.
     * <p>
     * Insert newInterval into intervals such that intervals is still sorted in ascending order by starti
     * and intervals still does not have any overlapping intervals (merge overlapping intervals if necessary).
     * <p>
     * Return intervals after the insertion.
     * <p>
     * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
     * Output: [[1,5],[6,9]]
     * <p>
     * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
     * Output: [[1,2],[3,10],[12,16]]
     * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
     * <p>
     * https://leetcode.com/problems/insert-interval/description/
     */
    @Test
    void testInsert() {
        int[][] input = {
                {1, 3}, {6, 9}
        };
        int[][] answer = {
                {1, 5}, {6, 9}
        };
        assertThat(insert(input, new int[]{2, 5})).isDeepEqualTo(answer);
        answer = new int[][]{
                {1, 3}, {4, 9}
        };
        assertThat(insert(input, new int[]{4, 7})).isDeepEqualTo(answer);
        input = new int[][]{
                {1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}
        };
        answer = new int[][]{
                {1, 2}, {3, 10}, {12, 16}
        };
        assertThat(insert(input, new int[]{4, 8})).isDeepEqualTo(answer);
    }

    /**
     * We keep track of newStart and newEnd init to the start and end of the inserted interval.
     * Iterate the array and for each interval, Checking current interval with those two states.
     * If newEnd < start (no overlapped), add the new interval to the result list and copy and
     * add all intervals to the result.
     * If end < newStart (no overlapped), add the current interval to the result.
     * Else (overlapped), update newStart to Math.min(start, newStart), newEnd to
     * Math.max(end, newEnd)
     * After loops ends, add the interval[newStart, newEnd] to the result.
     * <p>
     * Observation:
     * There are ONLY three cases when comparing an existing interval(i1) and the new one(i2)
     * No overlap:
     * 1. The new interval is ended before the current one, i.e. i2.end < i1.start
     * |---new---|...|---current---|
     * <p>
     * 2. The current interval is ended before the new one, i.e. i1.end < i2.start
     * |---current---|...|---new---|
     * <p>
     * Overlapped:
     * 3. Either start or end or both of one interval fall on the other's range. Anything beyond the above
     * two conditions must be overlapped.
     * <p>
     * <p>
     * Algo:
     * Iterate the intervals array, for each interval, we check the 3 cases above
     * - For case 1, regardless this happens at the first or the middle, since we know the new interval inserted
     * position, i.e. before the current one. We can add it to the result. Thus, there is no point to check
     * the rest of intervals, we can just add the current interval and the rest of them to the results as well.
     * Then return here.
     * <p>
     * - For case 2, just add the current interval to the result and continue
     * <p>
     * - For case 3, current interval and new interval are overlapped. So we need to perform the merge.
     * newStart = Math.min(currentStart, newStart)
     * newEnd = Math.max(currentEnd, newEnd)
     * Then we continue to use these new start and end as the new interval to compare the remaining intervals.
     * <p>
     * After iterating all intervals, add the newInterval to the result and return.
     * <p>
     * Time complexity: O(N).
     * Space complexity: O(1)
     */
    int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int intervalNum = intervals.length;
        // Use new var from new interval so we won't modify the input
        int newStart = newInterval[0];
        int newEnd = newInterval[1];
        for (int i = 0; i < intervals.length; i++) {
            int start = intervals[i][0];
            int end = intervals[i][1];
            if (newEnd < start) {
                // |---new---|
                //             |---current---|
                // new interval ends before current one, so no overlapping, just insert it into result
                result.add(new int[]{newStart, newEnd});
                // Since the new interval was inserted into result, no point to continue to check remaining intervals.
                // We can just append the rest of the intervals including this one and return.
                result.addAll(Arrays.asList(Arrays.copyOfRange(intervals, i, intervalNum)));
                return result.toArray(new int[0][0]);
            } else if (end < newStart) {
                // |---current---|
                //                 |---new---|
                // new interval starts after the current one, so no overlapping.
                // add the current interval to the list
                result.add(intervals[i]);
            } else {
                // current and new interval are overlapped, need to perform merge, update the newStart and newEnd, so
                // we can continue to check the remaining intervals
                newStart = Math.min(start, newStart);
                newEnd = Math.max(end, newEnd);
            }
        }
        // Add the new interval to the result. When we reach here, regardless it may be merged w/ other interval(s),
        // it must be the last interval after everyone in the intervals array
        result.add(new int[]{newStart, newEnd});
        return result.toArray(new int[0][0]);
    }

    /**
     * Non-overlapping Intervals
     * <p>
     * Given an array of intervals where intervals[i] = [starti, endi], return the minimum
     * number of intervals you need to remove to make the rest of the intervals non-overlapping.
     * <p>
     * Input: intervals = [[1,2],[2,3],[3,4],[1,3]]
     * Output: 1
     * Explanation: [1,3] can be removed and the rest of the intervals are non-overlapping.
     * <p>
     * Input: intervals = [[1,2],[1,2],[1,2]]
     * Output: 2
     * Explanation: You need to remove two [1,2] to make the rest of the intervals non-overlapping.
     * <p>
     * Input: intervals = [[1,2],[2,3]]
     * Output: 0
     * Explanation: You don't need to remove any of the intervals since they're already non-overlapping.
     * <p>
     * https://leetcode.com/problems/non-overlapping-intervals/description/
     */
    @Test
    void testEraseOverlapIntervals() {
        int[][] input = {
                {1, 2}, {2, 3}, {3, 4}, {1, 3}
        };

        assertThat(eraseOverlapIntervals(input)).isEqualTo(1);
        input = new int[][]{
                {1, 2}, {1, 2}, {1, 2}
        };
        assertThat(eraseOverlapIntervals(input)).isEqualTo(2);
        input = new int[][]{
                {1, 100}, {11, 22}, {1, 11}, {2, 12}
        };
        assertThat(eraseOverlapIntervals(input)).isEqualTo(2);
    }

    /**
     * Sorting the intervals by start time first. We use prevEnd, init to the first interval endTime,
     * as the baseline to compare the next interval to detect overlapping.
     * Start iterating from the second interval, if current interval startTime < prevEnd, we have
     * overlap. We want to keep the interval w/ the earlier endTime, so we update prevEnd to the
     * min of these two endTimes, and increment the removed count. Otherwise, update the prevEnd
     * to the current interval endTime.
     * <p>
     * Observation:
     * When there is an overlap between two intervals, it makes sense to remove the interval with a later end-time,
     * so we have a chance to accommodate more intervals in the future.
     * <p>
     * Algo:
     * Similar to other interval problem, we first sort the interval by start time and then check for conflict.
     * We maintain prevEnd as the end time of the previous interval and check if there is overlap against the
     * current interval. Whenever there is an overlap we need to remove the one w/ the later end time, which means
     * the prevEnd will be updated to the smaller end time. Then increment the removed count.
     * If no overlap, we just update the prevEnd to the current interval end time.
     * <p>
     * Note: Another implementation is sorting by endTime. Check LeetCode
     * <p>
     * Time complexity: O(n⋅logn)
     * The time complexity is dominated by sorting. Once the array has been sorted, only O(n) time is
     * taken to go through the array and determine if there is any overlap.
     * <p>
     * Space Complexity: O(1)
     */
    int eraseOverlapIntervals(int[][] intervals) {
        // Sort by start time
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        int removed = 0;
        int prevEnd = intervals[0][1]; // Used as the baseline when comparing the next interval
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] < prevEnd) {
                // overlapped due to current interval start time < previous interval end time
                // Greedy algo:
                // We want to keep the interval w/ earlier end time, so we can maximize the number of non-overlapping
                // intervals afterward. Hence, we update the prevEnd to the smaller end time between these two overlapped
                // interval
                prevEnd = Math.min(prevEnd, intervals[i][1]);
                // Increment removed cuz we discard one of these two intervals
                ++removed;
            } else {
                //no overlap, update the previous end time to the current end time
                prevEnd = intervals[i][1];
            }
        }
        return removed;
    }

    /**
     * Meeting Rooms
     * Given an array of meeting time intervals where intervals[i] = [starti, endi],
     * determine if a person could attend all meetings.
     * <p>
     * Input: intervals = [[0,30],[5,10],[15,20]]
     * Output: false
     * <p>
     * Input: intervals = [[7,10],[2,4]]
     * Output: true
     * https://leetcode.com/problems/meeting-rooms/description/
     */
    @Test
    void testCanAttendMeetings() {
        int[][] input = new int[][]{{0, 30}, {5, 10}, {15, 20}};
        Assertions.assertThat(canAttendMeetings(input)).isFalse();
        input = new int[][]{{7, 10}, {2, 4}};
        Assertions.assertThat(canAttendMeetings(input)).isTrue();
    }

    /**
     * First sort the array by starting time. Then iterate the array and check if the current meeting end
     * time is later than the next meeting start time. If so return false.
     * <p>
     * Time complexity : O(nlogn)
     * The time complexity is dominated by sorting. Once the array has been sorted, only O(n) time is
     * taken to go through the array and determine if there is any overlap.
     * <p>
     * Space complexity : O(1).
     */
    boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        for (int i = 0; i < intervals.length - 1; i++) {
            int currentEndTime = intervals[i][1];
            int nextStartTime = intervals[i + 1][0];
            if (currentEndTime > nextStartTime)
                return false;
        }
        return true;
    }

    /**
     * Meeting Rooms II
     * Given an array of meeting time intervals where intervals[i] = [starti, endi],
     * return the minimum number of conference rooms required.
     * <p>
     * Input: intervals = [[0,30],[5,10],[15,20]]
     * Output: 2
     * <p>
     * Input: intervals = [[7,10],[2,4]]
     * Output: 1
     * <p>
     * https://leetcode.com/problems/meeting-rooms-ii/description/
     */
    @Test
    void testMinMeetingRooms() {
        int[][] input = new int[][]{{0, 30}, {5, 10}, {15, 20}};
        Assertions.assertThat(minMeetingRooms(input)).isEqualTo(2);
        input = new int[][]{{7, 10}, {2, 4}};
        Assertions.assertThat(minMeetingRoomsMinHeap(input)).isEqualTo(1);
    }

    /**
     * Separate the start times and end times in two arrays and sort them respectively. Iterate the startTime array and
     * maintain another ptr on endTime array. If startTime < endTime, increment roomCount, otherwise advance endTime ptr
     * <p>
     * Observation:
     * 1. Imagine we plot every start and end time in a single timeline in chronological order. Instead of considering
     * a meeting (start/end time pair), we only consider each individual event and its meaning.
     * When we encounter an ending event, that means some meeting that started earlier has ended now. We are not
     * really concerned with which meeting has ended. All we need is that some meeting ended thus making a room available.
     * <p>
     * 2. When we encounter a start event, we need to know the earliest end time of any meeting, so we can decide
     * if we need a meeting room.
     * <p>
     * 3. Given two points above, we want to keep start and end time in separate array and sort them. So we can easily
     * compare each of them and keep track of the meeting end time.
     * <p>
     * Algo:
     * 1. Separate out the start times and the end times in their separate arrays and sort them respectively.
     * 2. Iterate the start time array and maintain another ptr(endIdx) on the end time array to track the latest end time of a room
     * - If the current start time < the latest (room) end time(end[endIdx]). We need a new room.
     * - Otherwise, it means some meeting has ended by the time this meeting starts. So we can reuse one of the rooms.
     * However, we also need to update the latest (room) end time by advancing the endIdx.
     * <p>
     * Time Complexity: O(NlogN) because all we are doing is sorting the two arrays for start timings and end timings
     * individually and each of them would contain N elements considering there are N intervals.
     * <p>
     * Space Complexity: O(N) because we create two separate arrays of size N, one for recording the start times and one
     * for the end times.
     */
    int minMeetingRooms(int[][] intervals) {
        // Populate start and end time arrays and sort them
        int[] start = new int[intervals.length];
        int[] end = new int[intervals.length];

        for (int i = 0; i < intervals.length; i++) {
            start[i] = intervals[i][0];
            end[i] = intervals[i][1];
        }
        Arrays.sort(start);
        Arrays.sort(end);

        int usedRoomsCount = 0;
        for (int startIdx = 0, endIdx = 0; startIdx < intervals.length; startIdx++) {
            if (start[startIdx] < end[endIdx])
                // Current start time is earlier than the latest end time, so we need a meeting room.
                // The room counter starts at 0, so the first start time will always increment room count
                usedRoomsCount++;
            else
                // start[startIdx] >= end[endIdx]
                // Current start time is equal or later than the latest end time. This mean a meeting has ended, so we
                // can reuse one of the existing rooms
                // Increment the end time ptr to track when the next meeting room will be available
                endIdx++;
        }
        return usedRoomsCount;
    }

    /**
     * Sort the array by the start time first. Use a MinHeap to track the end time of the rooms currently in use. Iterate the
     * array and if the start time >= top of the heap(latest end time of rooms), remove it from the heap.
     * We add the current meeting end time to the heap regardless. (Reuse this room w/ updated end time if the previous condition
     * is true, otherwise, it means we get a new room). Finally, the size of the heap is the total rooms we need.
     * <p>
     * Observation:
     * At any point in time we have multiple rooms that can be occupied, and we don't really care which room
     * is free as long as we find one when required for a new meeting.
     * <p>
     * Instead of manually iterating on every room that's been allocated and checking if the room is
     * available or not, we can keep all the rooms in a min heap where the key for the min heap would be
     * the ending time of meeting.
     * <p>
     * So, every time we want to check if any room is free or not, simply check the topmost element of the
     * min heap as that would be the room that would get free the earliest out of all the other rooms
     * currently occupied.
     * <p>
     * If the room we extracted from the top of the min heap isn't free, then no other room is.
     * So, we can save time here and simply allocate a new room.
     * <p>
     * Time Complexity: O(NlogN)
     * Sorting of the array that takes O(NlogN) considering that the array consists of N elements.
     * With the min-heap. In the worst case, all N meetings will collide with each other.
     * In any case we have N add operations on the heap. In the worst case we will have N extract-min
     * operations as well. Overall complexity being (NlogN) since extract-min operation on a heap takes O(logN).
     * <p>
     * Space Complexity: O(N)
     * Constructing the min-heap and that can contain N elements in the worst case as
     * described above in the time complexity section. Hence, the space complexity is O(N).
     * <p>
     * This Min Heap solution uses more memory than the two index start/end time iteration approach above due to the
     * PriorityQueue
     */
    int minMeetingRoomsMinHeap(int[][] intervals) {
        // Sort the intervals by start time
        Arrays.sort(intervals, Comparator.comparingInt(x -> x[0]));
        // Each item in the MinHeap is the end time of the meeting room(s) currently in use
        PriorityQueue<Integer> roomEndTimeMinHeap = new PriorityQueue<>();
        for (int[] interval : intervals) {
            int startTime = interval[0];
            if (!roomEndTimeMinHeap.isEmpty() && startTime >= roomEndTimeMinHeap.peek())
                // When the current meeting starts at or later than the meeting room ending the earliest in the MinHeap,
                // i.e. the top element, which means the meeting can use this room. So we remove it from heap to claim it.
                // (The room will be added to the heap w/ the updated end time later)
                roomEndTimeMinHeap.poll();
            // Add the meeting room w/ end time of this meeting to the heap, so we can keep track of the room
            // availability
            roomEndTimeMinHeap.offer(interval[1]);
        }
        return roomEndTimeMinHeap.size();
    }

    /**
     * Maximum Number of Events That Can Be Attended
     * You are given an array of events where events[i] = [startDay_i, endDay_i]. Every event i
     * starts at startDay_i and ends at endDay_i inclusive.
     * <p>
     * You can attend an event i at any day d where startDay_i <= d <= endDay_i. You can only attend
     * one event at any time d. For example, if events[i] = [1, 3], you could attend this event on
     * either day 1, day 2, or day 3.
     * <p>
     * Return the maximum number of events you can attend.
     * <p>
     * Input: events = [[1,2],[2,3],[3,4]]
     * Output: 3
     * Explanation: You can attend all the three events.
     * One way to attend them all is as shown.
     * Attend the first event on day 1.
     * Attend the second event on day 2.
     * Attend the third event on day 3.
     * <p>
     * <p>
     * Input: events= [[1,2],[2,3],[3,4],[1,2]]
     * Output: 4
     * Explanation:
     * [1,2] on Day 1,
     * [1,2] on Day 2, since the event has not ended and would only end on completion of Day 2
     * [2,3] on Day 3
     * [3,4] on Day 4
     * Hence Output = 4
     * <p>
     * https://leetcode.com/problems/maximum-number-of-events-that-can-be-attended/description/
     */
    @Test
    void testMaxEvents() {
        int[][] events = new int[][]{{1, 2}, {1, 2}, {3, 3}, {1, 5}, {1, 5}};
        Assertions.assertThat(maxEvents(events)).isEqualTo(5);
        events = new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 2}};
        Assertions.assertThat(maxEvents(events)).isEqualTo(4);
    }

    /**
     * First sort the events by start day. We use a heap, endDayMinHeap, to store end days of events
     * that have the start day equal to the current join day. We start the loop until we have checked
     * every event (ptr, i, to track) and nothing left in the endDayMinHeap.
     * For each iteration, if nothing left in the heap, use the events[i] start day as current join
     * day. Then add all events having the start day the same as joinDay to the heap.
     * We join an event by removing one item from the heap and increment eventCount and joinDay.
     * Finally, we need to check the heap and remove items that is less than updated joinDay.
     * <p>
     * Observation:
     * 1. First we always join the event started earlier, so the intuition is to sort the events by
     * the start day. Then when we have more than one event starting at the same day, we want to join
     * the one that has earlier end day, so we will have more chance to join others. This is the greedy
     * part.
     * <p>
     * 2. Given the thought process above, after we pick a day to join from the sorted start day, we
     * put all the events with the same start day into a endDayMinHeap. Then take the top one from
     * the heap as we join that event and increment the event count and joinDay.
     * <p>
     * 3. After we join a event and update the joinDay, it is possible that the events previous added
     * to the heap can't be joined anymore, i.e. their endDay is less than the new joinDay
     * <p>
     * Time complexity: O(NlogN)
     * Space complexity: O(N)
     */
    int maxEvents(int[][] events) {
        if (events.length == 1) {
            return 1;
        }
        // Sort events by staring time
        Arrays.sort(events, Comparator.comparingInt(e -> e[0]));
        // heap holds all events having the start day <= joinDay
        Queue<Integer> endDayMinHeap = new PriorityQueue<>();
        int eventCount = 0;
        int joinDay = 0;
        int i = 0; // ptr for checking every event
        // Loop until we have checked every event and nothing left in the heap
        while (i < events.length || !endDayMinHeap.isEmpty()) {
            // If the heap is empty, we use start day of the current event i as the join day.
            // This condition is needed even after the first iteration, cuz even tho there are multiple events added
            // to the heap earlier, they may be removed after we join one of them. For example, multiple one-day events
            // on the same day
            if (endDayMinHeap.isEmpty()) {
                joinDay = events[i][0];
            }
            // Now add all events having the start day the same as joinDay to the heap
            while (i < events.length && events[i][0] == joinDay) {
                endDayMinHeap.offer(events[i][1]);
                i++;
            }
            // Join the earliest ending event. We want to join the event ended earlier. GREEDY
            endDayMinHeap.poll();
            eventCount++;
            joinDay++;
            // After we update the joinDay, we need to check the heap to remove already ended events.
            while (!endDayMinHeap.isEmpty() && endDayMinHeap.peek() < joinDay) {
                endDayMinHeap.poll();
            }
        }
        return eventCount;
    }

}
