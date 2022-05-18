package com.bookbae.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.ArrayList;

public class BookList {
    @JsonProperty
    public List<BookListEntry> entries;
    public BookList() {
        this.entries = new ArrayList<BookListEntry>();
    }
}