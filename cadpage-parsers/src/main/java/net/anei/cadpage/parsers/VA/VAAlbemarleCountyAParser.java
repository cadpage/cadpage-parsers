package net.anei.cadpage.parsers.VA;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Albemarle County, VA
 */

public class VAAlbemarleCountyAParser extends FieldProgramParser {
  
  private static final Pattern SRC_PTN = Pattern.compile("((?:ALL )?[A-Z0-9]+) +(.*)");
  private static final Pattern TAIL_JUNK_PTN = Pattern.compile(":?[?=<>;][?=<>;:\\d]*$");
  private static final Pattern GEN_ALERT_PTN = Pattern.compile("TIME: (\\d\\d:\\d\\d) +(.*)");
  private static final Pattern COUNTY_ADDR_PTN = Pattern.compile("([A-Z]+) +\\d+");
  
  public VAAlbemarleCountyAParser() {
    super(CITY_CODES, "ALBEMARLE COUNTY", "VA",
          "( PRI:PRI_ID! TYP:CALL | CALL! ) APT:APT? AD:ADDR! CTY:CITY! LOC:PLACE? CMT1:INFO? CMT2:INFO? TIME:TIME_UNIT XST:X? XST2:X?");
  }
  
  @Override
  public String getFilter() {
    return "CAD@acuecc.org,CAD2@acuecc.org ";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    
    Matcher match = SRC_PTN.matcher(body);
    if (!match.matches()) return false;
    data.strSource = match.group(1).trim();
    body = match.group(2);
    
    // Strip duplicate source name and other junk from end of body
    body = stripFieldEnd(body, ' ' + data.strSource);
    body = TAIL_JUNK_PTN.matcher(body).replaceAll("");
    
     match = GEN_ALERT_PTN.matcher(body);
    if (match.matches()) {
      setFieldList("TIME INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strTime =  match.group(1);
      data.strSupp = match.group(2);
      return true;
    }

    body = body.replace("TIME:", " TIME:");
    if (!super.parseMsg(body, data)) return false;
    
    // Mutual aid calls outside the county save the address
    // in the place field and the county name/district in the address
    if (data.strPlace.length() > 0 && data.strCity.length() == 0) {
      match = COUNTY_ADDR_PTN.matcher(data.strAddress);
      if (match.matches()) {
        data.strCity = match.group(1) + " COUNTY";
        String addr = data.strPlace;
        int pt = addr.indexOf(" - ");
        if (pt >= 0) addr = addr.substring(0,pt).trim();
        data.strPlace = data.strAddress;
        data.strAddress = "";
        parseAddress(addr, data);
      }
    }
    
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI_ID")) return new MyPriorityIdField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("INFO")) return new MyInfoField();
    if (name.equals("TIME_UNIT")) return new MyTimeUnitField();
    return super.getField(name);
  }
  
  private static final Pattern PRI_ID_PTN = Pattern.compile("(\\d) +FAF\\d{7}(\\d{5})");
  private class MyPriorityIdField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = PRI_ID_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strPriority = match.group(1);
      data.strCallId = match.group(2);
    }

    @Override
    public String getFieldNames() {
      return "PRI ID";
    }
  }
  
  // Address field may contain place name
  private class MyAddressField extends AddressField {
    
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(" - ");
      if (pt >= 0) {
        data.strPlace = field.substring(pt+3).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data); 
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " PLACE";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      int pt = field.indexOf(' ');
      if (pt >= 0) {
        data.strSupp = field.substring(pt+1).trim();
        field = field.substring(0,pt);
      }
      super.parse(field, data);
    }
    
    @Override
    public String getFieldNames() {
      return "CITY INFO";
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.startsWith("Original Location :")) {
        String  place = field.substring(19).trim();
        if (!data.strPlace.contains(place)) {
          data.strPlace = append(data.strPlace, " - ", place);
        }
      } 
      else {
        if (field.startsWith("INCIDENT CLONED FROM PARENT:")) return;
        data.strSupp = append(data.strSupp, "\n", field);
      }
    }
  }
  
  private static final Pattern TIME_UNIT_PTN = Pattern.compile("(\\d\\d:\\d\\d) *(.*?)(?: ((?!AF|CF).*))?");
  private class MyTimeUnitField extends Field {

    @Override
    public void parse(String field, Data data) {
      Matcher match = TIME_UNIT_PTN.matcher(field);
      if (!match.matches()) {
        if ("NN:NN".startsWith(field.replaceAll("\\d", "N"))) return;
        abort();
      }
      data.strTime = match.group(1);
      String unit = ' ' +match.group(2);
      unit = unit.replace(" AF", " ").replace(" CF", " ");
      data.strUnit = unit.substring(1);
      data.strSupp = append(data.strSupp, "\n", getOptGroup(match.group(3)));
    }

    @Override
    public String getFieldNames() {
      return "TIME UNIT";
    }
  }

  @Override
  public String adjustMapAddress(String sAddress, boolean cross) {
    return sAddress.replace("LEWIS AND CLARK", "LEWIS_AND_CLARK");
  }

  @Override
  public String postAdjustMapAddress(String sAddress) {
    return sAddress.replace("LEWIS_AND_CLARK", "LEWIS AND CLARK");
  }
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      "AC", "",
      "CH", "CHARLOTTESVILLE",
      "SC", "SCOTTSVILLE"
  });
}
