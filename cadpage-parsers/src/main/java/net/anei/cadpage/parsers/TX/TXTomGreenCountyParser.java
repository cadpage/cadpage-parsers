package net.anei.cadpage.parsers.TX;

import java.util.Properties;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

public class TXTomGreenCountyParser extends FieldProgramParser {

  public TXTomGreenCountyParser() {
    super(CITY_CODES, "TOM GREEN COUNTY", "TX",
          "DISPATCH_TIME:TIME! Incident:ID! Type:CALL! Address:EMPTY! EMPTY+? ADDR EMPTY+? Jurisdiction:CITY! Cross_streets:X! EMPTY+? Units_Dispatched:EMPTY! UNIT! END");
  }

  @Override
  public String getFilter() {
    return "safd-alerting@cosatx.us,jpristelski@usdd.com,no-reply@fsa-mobile.com,safd-alerting@sanangelo.gov";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.startsWith("ALERT - ")) return false;
    data.strSource = subject.substring(8).trim();
    int pt = body.indexOf("\n\n\nThis message");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (body.startsWith("DISPATCH TIME:")) {
      return parseFields(body.split("\n"), data);
    } else {
      setFieldList("INFO");
      data.msgType = MsgType.GEN_ALERT;
      data.strSupp = body;
      return true;
    }
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d", true);
    if (name.equals("CALL")) return new CallField("\\( *(.*?) *\\)", true);
    if (name.equals("ADDR")) return new MyAddressField();
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("UNIT")) return new MyUnitField();
    return super.getField(name);
  }

  private class MyAddressField extends AddressField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      int pt = field.indexOf(';');
      if (pt >= 0) {
        apt = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private class MyCityField extends CityField {
    @Override
    public void parse(String field, Data data) {
      if (field.isEmpty()) return;
      data.strAddress = stripFieldEnd(data.strAddress, ' ' + field);
      super.parse(field, data);
    }
  }

  private class MyUnitField extends UnitField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace(' ', ',');
      super.parse(field, data);
    }
  }

  private static final Properties CITY_CODES = buildCodeTable(new String[] {
      "CB",  "Carlsbad",
      "CH",  "Christoval",
      "DC",  "Dove Creek",
      "EC",  "East Concho",
      "EO",  "Eola",
      "GC",  "Grape Creek",
      "KN",  "Knickerbocker",
      "MR",  "Mereta",
      "PC",  "Pecan Creek",
      "SJT", "San Angelo",    // ???
      "TGC", "Tom Green County",
      "QV",  "Quail Valley",
      "WL",  "Wall",
      "WV",  "Water Valley"
  });
}
