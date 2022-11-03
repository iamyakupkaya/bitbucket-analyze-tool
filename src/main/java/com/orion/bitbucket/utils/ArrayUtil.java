package com.orion.bitbucket.utils;

import java.util.ArrayList;
import java.util.Collection;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Component;

@Component
public class ArrayUtil
{
    public ArrayList<Object> convert(JSONArray jArr)
    {
        ArrayList<Object> list = new ArrayList<Object>();
        try {
            for (int i=0, l=jArr.length(); i<l; i++){
                list.add(jArr.get(i));
            }
        } catch (JSONException e) {}

        return list;
    }

    public JSONArray convert(Collection<Object> list)
    {
        return new JSONArray(list);
    }

}