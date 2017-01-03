package cz.zcu.fav.kiv.ups.view;



/**
 * Created by vrenclouff on 07.12.16.
 */
public class ViewDTO {

    private Class aClass;

    private Object[] objects;

    public ViewDTO(Class aClass , Object[] objects) {
        this.aClass = aClass;
        this.objects = objects;
    }

    public Class getaClass() {
        return aClass;
    }

    public Object[] getObjects() {
        return objects;
    }

}
