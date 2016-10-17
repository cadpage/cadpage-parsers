package net.anei.cadpage.parsers.VA;

import java.util.Properties;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.dispatch.DispatchOSSIParser;

/**
 * Prince George County, VA
 */
public class VAPrinceGeorgeCountyParser extends DispatchOSSIParser {
  
  private String version;
 
  public VAPrinceGeorgeCountyParser() {
    super(CITY_CODES, "PRINCE GEORGE COUNTY", "VA",
          "EMPTY? ( CANCEL ADDR CITY | CALL ADDR ( SELECT/UNIT CITY/Y | UNIT ( SELECT/AUTO ID! | X/Z+? ( CITY/Y ID? | ID! ) ) ) ) INFO+");
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    data.strUnit = subject;
    version = subject.length() > 0 ? "UNIT" : "NOUNIT";
    return super.parseMsg("CAD:" + body, data);
  }
  
  @Override
  public String getProgram() {
    String prog = super.getProgram();
    if (!prog.contains("UNIT")) prog = "UNIT " + prog;
    else if (!prog.contains("CITY")) prog = prog.replace("CALL", "CALL CITY");
    return prog;
  }
  
  @Override
  protected String getSelectValue() {
    return version;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("ID")) return new IdField("\\d{10}", true);
    return super.getField(name);
  }

  private static final String AUTO_AID_PFX = "FIRE AUTOMATIC AID - ";
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith(AUTO_AID_PFX)) {
        version = "AUTO";
        data.strCity = field.substring(AUTO_AID_PFX.length()).trim();
        field = field.substring(0,AUTO_AID_PFX.length()-3).trim();
      }
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES= buildCodeTable(new String[]{
      "CARS", "CARSON",
      "CHAR", "CHARLES CITY",
      "CHES", "CHESTER",
      "CHGT", "COLONIAL HEIGHTS",
      "DEW",  "DEWITT",
      "DIN",  "DINWIDDIE",
      "DISP", "DISPUTANTA",
      "FTLE", "FT LEE",
      "HOPE", "HOPEWELL",
      "MAT",  "MATOACA",
      "MID",  "MIDLOTHIAN",
      "PETE", "PETERSBURG",
      "PORT", "PORTSMOUTH",
      "PRG",  "PRINCE GEORGE",
      "RIC",  "RICHMOND",
      "SPRI", "SPRING GROVE",
      "STON", "STONY CREEK",
      "SUR",  "SURRY",
      "SUS",  "SUSSEX",
      "WAV",  "WAVERLY"
  });
}
