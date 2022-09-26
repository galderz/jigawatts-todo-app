package org.acme;

public record Todo(
    Long id
    , String title
    , int order
    , boolean completed
    , String url
)
{
    Todo withId(long id) {
        return new Todo(id, this.title, this.order, this.completed, this.url);
    }
}
