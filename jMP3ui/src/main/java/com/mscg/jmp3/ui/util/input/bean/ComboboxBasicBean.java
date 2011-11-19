package com.mscg.jmp3.ui.util.input.bean;

import java.io.Serializable;

public class ComboboxBasicBean implements Serializable {

    private static final long serialVersionUID = -2357200602635042164L;

    private String key;
    private String value;

    public ComboboxBasicBean(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        ComboboxBasicBean other = (ComboboxBasicBean) obj;
        if(key == null) {
            if(other.key != null)
                return false;
        } else if(!key.equals(other.key))
            return false;
        return true;
    }

}
