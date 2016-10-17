package net.anei.cadpage.parsers.GA;

import java.util.Properties;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

public class GAClaytonCountyParser extends DispatchOSSIParser {
  
  public GAClaytonCountyParser() {
    super(CITY_CODES, "CLAYTON COUNTY", "GA",
           "FYI? ( UNIT_CALL ADDR ( CITY | PLACE ) CALL | CALL DATETIME ADDR! ) INFO+");
  }
  
  @Override
  public String getFilter() {
    return "CAD@co.clayton.ga.us";
  }

  public class UnitCallField extends CallField {
       
    @Override 
    public boolean canFail(){
      return true;
    }
    
    @Override 
    public boolean checkParse(String field, Data data){
      int i = field.indexOf("{");
      int j = field.indexOf("}");
      if (i == 0){
        if (j >= 0){
          String Call = field.substring(j + 1).trim();
          Call.trim();
          String Unit = field.substring(i + 1, j).trim();
          Unit.trim();
          data.strCall = Call;
          data.strUnit = Unit;
          return true;
        }
      }
      if (field.startsWith("CANCEL")){
        data.strCall = field;
        return true;
      }
      return false;
    }
    
    @Override
    public void parse(String body, Data data){
      if (!checkParse(body, data))
        abort();
    }
    
    @Override
    public String getFieldNames(){
      return "UNIT CALL";
    }
  }

  @Override
  protected Field getField(String name){
    if (name.equals("UNIT_CALL")) return new UnitCallField();
    if (name.equals("DATETIME")) return new DateTimeField("\\d\\d/\\d\\d/\\d{4} +\\d\\d:\\d\\d:\\d\\d", true);
    return super.getField(name);
  }
      
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
      //CITIES
      "COLL", "COLLEGE PARK",
      "FORE", "FOREST PARK",
      "HAMP", "HAMPTON",
      "JONE", "JONESBORO",
      "LAKE", "LAKE CITY",
      "LOVE", "LOVEJOY",
      "MORR", "MORROW",
      "RIVE", "RIVERDALE",
      "STOC", "STOCKBRIDGE",
      "ATLA", "ATLANTA",
      
      //UNINCORPORATED AREAS
      "REX",  "REX",
      "CONL", "CONLEY", 
      "ELLE", "ELLENWOOD",
      "IRON", "IRONDALE",
      "BONA", "BONANZA",
  });
}