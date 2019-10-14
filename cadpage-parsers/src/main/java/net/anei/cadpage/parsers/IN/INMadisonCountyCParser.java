package net.anei.cadpage.parsers.IN;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

/**
 * Madison County, IN (C)
 */
public class INMadisonCountyCParser extends FieldProgramParser {
  
  public INMadisonCountyCParser() {
    super("MADISON COUNTY", "IN",
          "( DATE:DATETIME! CFS#:SKIP? PLACE:PLACE! ADDR:ADDRCITY! ( CROSS_STREETS:X! CALL:CALL! UNIT:UNIT! | CALL:CALL! UNIT:UNIT! CROSS_STREETS:X! ) INFO:INFO! FIRE_RD:CH! EMS_RD:CH ( CAD_#:ID! GPS_LAT:GPS1! GPS_LON:GPS2! NARRATIVE:INFO/N! INFO/N+ | RUN_#:ID! NARRATIVE:INFO/N! INFO/N+ CALLER-NAME:NAME! CALLER-PHONE:PHONE! CAD_#:SKIP! ALL_INCIDENTS:ID! GPS_LAT:GPS1! GPS_LON:GPS2! ) " +
          "| CALL:CALL! DATE:DATETIME! PLACE:PLACE! ADDR:ADDRCITY! INFO:INFO? ( MAP:MAP! CITY:CITY! ID:ID! PRI:PRI! UNIT:UNIT! X:X! SOURCE:SKIP! | UNIT:UNIT! X:X! MAP:MAP! ) CALLER-NAME:NAME! CALLER-PHONE:PHONE! INCIDENT#:SKIP! OTHER_INCIDENT#:SKIP? DISTRICT:SKIP? BEAT:MAP! LOCATION_DETAILS:INFO/N+ NARRATIVE:INFO/N+ RADIO_CHANNEL:CH ) END");
  }
  
  @Override
  public String getFilter() {
    return "cfs@madisoncountypaging.com";
  }
  
  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
  
  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("CFS")) return false;
    body = body.replace("\nINTERSECTION:", "\nX:");
    return super.parseFields(body.split("\n"), data);
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new DateTimeField(new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa"), true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("X")) return new MyCrossField();
    if (name.equals("MAP")) return new MyMapField();
    if (name.equals("CH")) return new MyChannelField();
    if (name.equals("ID")) return new MyIdField();
    return super.getField(name);
  }
  
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      field = field.replace('@', '&');
      super.parse(field, data);
    }
  }
  
  private class MyCrossField extends CrossField {
    @Override
    public void parse(String field, Data data) {
      if (field.equals("No Cross Streets Found")) return;
      super.parse(field, data);
    }
  }
  
  private class MyMapField extends MapField {
    @Override
    public void parse(String field, Data data) {
      data.strMap = append(data.strMap, "-", field);
    }
  }
  
  private class MyChannelField extends ChannelField {
    @Override
    public void parse(String field, Data data) {
      if (field.length() == 0) return;
      if (field.equals(data.strChannel)) return;
      data.strChannel = append(data.strChannel, "/", field);
    }
  }
  
  private static final Pattern ID_JUNK_PTN = Pattern.compile(" *\\(\\w+\\)");
  private class MyIdField extends IdField {
    @Override
    public void parse(String field, Data data) {
      field = ID_JUNK_PTN.matcher(field).replaceAll("");
      super.parse(field, data);
    }
  }
  
  @Override
  public String adjustMapAddress(String address) {
    return CORD_PTN.matcher(address).replaceAll("");
  }
  private static final Pattern CORD_PTN = Pattern.compile("\\bCORD\\b");
}
