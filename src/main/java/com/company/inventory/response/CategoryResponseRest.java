package com.company.inventory.response;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class CategoryResponseRest extends ResponseRest{
    private CategoryResponse categoryResponse = new CategoryResponse();
}
