package com.jacobarau.mincast.sync.rss;

import com.jacobarau.mincast.subscription.Item;
import com.jacobarau.mincast.subscription.Subscription;

import java.util.Arrays;
import java.util.List;

public class ParseResult {
    public Subscription subscription;
    public List<Item> items;

    @Override
    public String toString() {
        return "ParseResult{" +
                "subscription=" + subscription +
                ", items=" + Arrays.toString(items.toArray()) +
                '}';
    }
}
