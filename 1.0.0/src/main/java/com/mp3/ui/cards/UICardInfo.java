package com.mp3.ui.cards;

import javax.swing.JPanel;

public class UICardInfo implements Comparable<UICardInfo> {

    private int index;
    private String name;
    private JPanel card;

    public UICardInfo(int index, String name, JPanel card) {
        this.index = index;
        this.name = name;
        this.card = card;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public JPanel getCard() {
        return card;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        return result;
    }

   @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        UICardInfo other = (UICardInfo) obj;
        if(index != other.index)
            return false;
        return true;
    }

    public int compareTo(UICardInfo o) {
        if(index > o.index)
            return 1;
        else if(index == o.index)
            return 0;
        else
            return -1;
    }

}
