package net.anei.cadpage.parsers.KY;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class KYMeadeCountyParser extends FieldProgramParser {

  public KYMeadeCountyParser() {
    super(CITY_LIST, "MEADE COUNTY", "KY", "CALL! CALL+? ( GADDR GADDR X! | ADDR/Zs X! | ADDR/Zs CITY ST? X! ) INFO+");
  }

  private static Pattern SOURCE = Pattern.compile("(.*?) *\\*{2}(.*)\\*{2}");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // Remove and save trailing **SRC** construct (reject if no match)
    Matcher mat = SOURCE.matcher(body);
    if (!mat.matches()) return false;
    body = mat.group(1);
    data.strSource = getOptGroup(mat.group(2));

    return super.parseFields(body.split(","), data);
  }

  @Override
  public String getProgram() {
    return super.getProgram() + " SRC";
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ST")) return new MyStateField();
    if (name.equals("GADDR")) return new MyGADDRField();
    if (name.equals("ADDR")) return new MyAddressField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    
    @Override
    public void parse(String field, Data data) {
      if (data.strCall.length() == 0) super.parse(field, data);
      else {
        if (KNOWN_CALLS.contains(field)) data.strCall = append(data.strCall, ", ", field);
        else {
          //if not a known call word, give to strPlace
          data.strPlace = append(data.strPlace, " - ", field);
        }
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL PLACE";
    }
  }
  
  private static Pattern CLEAN_CROSS = Pattern.compile("[ /]*(.*?)[ /]*");

  // checkParse, reject blank X fields, and replace // with /
  private class MyCrossField extends CrossField {

    @Override
    public boolean checkParse(String field, Data data) {
      if (!field.contains("//")) return false;
      // strip leading or trailing slashes/spaces
      Matcher mat = CLEAN_CROSS.matcher(field.replace("//", "/"));
      if (!mat.matches()) abort();
      super.parse(mat.group(1), data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }
  }

  // accept blank cities
  private class MyCityField extends CityField {

    @Override
    public boolean checkParse(String field, Data data) {
      if (field.length() == 0) return true;
      return super.checkParse(field, data);
    }

  }

  // validation pattern and support zip codes
  private class MyStateField extends StateField {
    public MyStateField() {
      super("([A-Z]{2}) *(?:\\d{5})?", true);
    }

    @Override
    public void parse(String field, Data data) {
      if (!field.equals("KY")) data.strState = field;
    }
  }

  // validation pattern and append with ", "
  private class MyGADDRField extends AddressField {

    public MyGADDRField() {
      super(" *(-?\\d{1,3}\\.\\d+)", true);
    }

    @Override
    public void parse(String field, Data data) {
      data.strAddress = append(data.strAddress, ", ", field);
    }
  }
  
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf('/');
      if (pt >= 0) {
        String part1 = field.substring(0,pt).trim();
        String part2 = field.substring(pt+1).trim();
        if (!isValidAddress(part1) && checkAddress(part2) == STATUS_FULL_ADDRESS) {
          data.strPlace = part1;
          field = part2;
        } else if (!isValidAddress(part2) && checkAddress(part1) == STATUS_FULL_ADDRESS) {
          data.strPlace = part2;
          field = part1;
        }
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "PLACE ADDR APT";
    }
  }

  private static String[] CITY_LIST = new String[] { 
    "BATTLETOWN", 
    "BRANDENBURG", 
    "ELIZABETHTOWN", 
    "EKRON", 
    "FLAHERTY",
    "FORT KNOX",
    "GUSTON", 
    "KY",
    "MULDRAUGH", 
    "PAYNEVILLE",
    "RHODELIA",
    "VINE GROVE" };
  
  Set<String> KNOWN_CALLS = new HashSet<String>(Arrays.asList(
      "MANUFACTURING", 
      "POSSESSION",
      "FIELD",
      "WOODS"));

}
