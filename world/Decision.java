package world;


public class Decision {
    private String literalValue;

    public Decision(String literalValue){
        this.literalValue = literalValue;
    }

    public int getInt(){
        return Integer.parseInt(literalValue);
    }
    public String getString(){
        return literalValue;
    }
    public boolean getBoolean(){
        if(literalValue.equals("1")){
            return true;
        } else {
            return false;

        }
    }
    public void setDecision(int i){
        literalValue = Integer.toString(i);
    }

    public void setDecision(boolean b){
        literalValue = b ? "1" : "0";
    }

    public void setDecision(String s){
        literalValue = s;
    }
}
