package net.anei.cadpage.parsers.PA;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;
import net.anei.cadpage.parsers.dispatch.DispatchProQAParser;

public class PAAlleghenyCountyDParser extends DispatchProQAParser {
  
  public PAAlleghenyCountyDParser() {
    super("ALLEGHENY COUNTY", "PA", 
          "CALL CALL/L+? ADDR/Z APT CITY! ZIP ( UNKNOWN | NAME NAME/CS ) INFO/L+", true);
    setupGpsLookupTable(PAAlleghenyCountyParser.GPS_TABLE_LOOKUP);
    setupPlaceGpsLookupTable(PAAlleghenyCountyParser.PLACE_GPS_LOOKUP_TABLE);
  }
  
  @Override
  protected void setProgram(String program, int flags) {
    
    // We do not have a any kind of call ID field 
    program = stripFieldStart(program, "ID! ");
    super.setProgram(program, flags);
  }

  @Override
  public String getFilter() {
    return "noreply@zollhosted.com";
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("APT")) return new MyAptField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("UNKNOWN")) return new NameField("<Unknown>", true);
    return super.getField(name);
  }
  
  private class MyAptField extends AptField {
    @Override
    public void parse(String field, Data data) {
      field = stripFieldStart(field, "APT");
      super.parse(field, data);
    }
  }

  private static final Pattern ZIP_PTN = Pattern.compile("\\d{5}");
  private class MyZipField extends SkipField {
    
    @Override
    public boolean canFail() {
      return true;
    }
    
    @Override
    public boolean checkParse(String field, Data data) {
      if (isLastField()) return true;
      return ZIP_PTN.matcher(field).matches();
    }
    
  }
  
}
