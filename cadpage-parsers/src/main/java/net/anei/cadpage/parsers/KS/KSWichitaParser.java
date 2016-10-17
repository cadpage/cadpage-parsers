package net.anei.cadpage.parsers.KS;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KSWichitaParser extends FieldProgramParser {

  public KSWichitaParser() {
    super(CITY_CODES, "WICHITA", "KS", "CALL ADDRCITY MAP ID UNIT SRC TIME! INFO+");
  }
  
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!body.startsWith("** DISP ** ")) return false;
    body = body.substring(11).trim();
    return parseFields(body.split("\\|"), data);
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ADDRCITY")) return new MyAddressField();
    if (name.equals("ID")) return new MyIdField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private static Pattern STRIP_COORDS = Pattern.compile("(.*) <.*,.*>");
  private static Pattern AT_PLACE = Pattern.compile("(.*)@(.*)");
  private static Pattern PAREN_PLACE = Pattern.compile("(.*) \\((.*)\\)");
  public class MyAddressField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = STRIP_COORDS.matcher(field);
      if (mat.matches()) field = mat.group(1).trim();
      
      //determine format based on presence of @ or (
      mat = AT_PLACE.matcher(field);
      if (mat.matches()) {
        data.strPlace = mat.group(1).trim();
        field = mat.group(2).trim();
      }
      mat = PAREN_PLACE.matcher(field);
      if (mat.matches()) {
        data.strPlace = mat.group(2).trim();
        field = mat.group(1).trim();
      }
      super.parse(field, data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  public class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      //remove leading $
      field = stripFieldStart(field, "$");
      super.parse(field, data);
    }
  }
  
  public class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      //remove leading "\*\* ?"
      field = stripFieldStart(field, "**");
      super.parse(field, data);
    }
  }
  
  private static Properties CITY_CODES = buildCodeTable(new String[]{
      "AN", "ANDALE",
      "BA", "BEL AIRE",
      "BE", "BENTLEY",
      "CH", "CHENEY",
      "CL", "CLEARWATER",
      "CO", "COLWICH",
      "DE", "DERBY",
      "EB", "EASTBOROUGH",
      "FU", "FURLEY",
      "GO", "GODDARD",
      "GP", "GARDEN PLAIN",
      "GR", "GREENWICH",
      "HA", "HAYSVILLE",
      "KE", "KECHI",
      "MA", "MAIZE",
      "MC", "MCCONNELL",
      "MH", "MT HOPE",
      "MU", "MULVANE",
      "PC", "PARK CITY",
      "PE", "PECK",
      "SC", "SEDGWICK COUNTY",
      "SH", "SCHULTE",
      "VC", "VALLEY CENTER",
      "VI", "VIOLA",
      "WI", "WICHITA"
  });

}
