package cz.zcu.fav.kiv.ups.view;


import cz.zcu.fav.kiv.ups.core.InternalMsg;

/**
 * Created by vrenclouff on 07.12.16.
 */
public class ViewDTO {

    private Class aClass;

    private String[] objects;

    public ViewDTO(Class aClass , String[] objects) {
        this.aClass = aClass;
        this.objects = objects;
    }

    public Class getaClass() {
        return aClass;
    }

    public String[] getObjects() {
        return objects;
    }

}
