package net.anei.cadpage.parsers.TX;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXKendallCountyBParser extends FieldProgramParser {
  
  public TXKendallCountyBParser() {
    super(CITY_LIST, "KENDALL COUNTY", "TX", 
          "CFS:ID! CALLTYPE:CALL! PRIORITY:PRI! PLACE:PLACE! ADDRESS:ADDR/S! CITY:CITY!  STATE:ST! ZIP:ZIP! DATE:DATE! TIME:TIME! UNIT:UNIT! INFO:INFO! INFO/N+ NAME:NAME ADDRESS:SKIP PHONE:PHONE INFO/N+");
  }
  
  @Override
  public String getFilter() {
    return "tyler@boerne-tx.gov,donotreply2@cityofboerne.net";
  }
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    return parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("PRI")) return new MyPriorityField();
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("ST")) return new MyStateField();
    if (name.equals("ZIP")) return new MyZipField();
    if (name.equals("DATE")) return new DateField("\\d\\d/\\d\\d/\\d{4}", true);
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d:\\d\\d", true);
    if (name.equals("UNIT")) return new MyUnitField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }
  
  private class MyPriorityField extends PriorityField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("NONE")) return;
      super.parse(field, data);
    }
  }

  private static final Pattern ADDR_ST_ZIP = Pattern.compile("(.*?)(?: (TX))?(?: (\\d{5}))?");
  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String zip = null;
      Matcher match = ADDR_ST_ZIP.matcher(field);
      if (match.matches()) {
        field = match.group(1).trim();
        String st = match.group(2);
        if (st != null) data.strState = st;
        zip = match.group(3);
      }
      field = stripFieldEnd(field, ",");
      int pt = field.lastIndexOf(',');
      if (pt >= 0) {
        parseAddress(field.substring(0,pt).trim(), data);
        data.strCity = field.substring(pt+1).trim();
      } else {
        super.parse(field, data);
      }
      if (data.strCity.length() == 0 && zip != null) data.strCity = zip;
      data.strAddress = stripFieldEnd(data.strAddress, ' ' + data.strCity);
    }
    
    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " ST";
    }
  }
  
  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private class MyStateField extends StateField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      super.parse(field, data);
    }
  }
  
  private class MyZipField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (data.strCity.length() == 0) {
        super.parse(field, data);
      }
    }
  }
  
  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(", ", ",").replace(' ', '_');
      super.parse(field, data);
    }
  }
  
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("Alert:")) return;
      super.parse(field, data);
    }
  }
  
  private static final String[] CITY_LIST = new String[]{

    // Cities
    "BOERNE",
    "FAIR OAKS RANCH",

    // Census-designated place
    "COMFORT",

    // Other unincorporated communities
    "ALAMO SPRINGS",
    "BERGHEIM",
    "KENDALIA",
    "KREUTZBERG",
    "LINDENDALE",
    "NELSON CITY",
    "OBERLY CROSSING",
    "PLEASANT VALLEY",
    "SISTERDALE",
    "WARING",
    "WALNUT GROVE",

    // Ghost towns
    "AMMANS CROSSING",
    "BANKERSMITH",
    "BENTON",
    "BLOCK CREEK",
    "CURREY'S CREEK",
    "SCHILLER",
    "WASP CREEK",
    "WELFARE",
    "WINDSOR"
  };
}
