
package com.example.schedulemydiet.models.food;

import java.io.Serializable;
import java.util.List;


public class Digest implements Serializable {

    public String label;
    public String tag;
    public Object schemaOrgTag;
    public Double total;
    public Boolean hasRDI;
    public Double daily;
    public String unit;
    public List<Digest> sub;

}
