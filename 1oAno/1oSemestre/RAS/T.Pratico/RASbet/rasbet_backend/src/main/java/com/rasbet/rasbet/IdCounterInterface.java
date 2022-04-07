package com.rasbet.rasbet;

import java.util.Set;

public interface IdCounterInterface {

    default int getCounter(Set<String> list) {
        int maxCounter = -1;
        int curr = -1;
        for (String elem : list) {
            curr = Integer.parseInt(elem.substring(1));
            if (curr > maxCounter)
                maxCounter = curr;
        }
        maxCounter++;
        return maxCounter;
    }
}