package com.mycompany.app;

import java.util.List;

public interface IRepository {
    List<Person> findEndorments(int personId, int depth);
}