package net.anei.cadpage.parsers.TX;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class TXDentonCountyEParser extends FieldProgramParser {


  public TXDentonCountyEParser() {
    super("DENTON COUNTY", "TX",
          "Msg_ID:SKIP! CALL? SRC/Z ID PLACE? CALL/SDS ADDR_INFO GPS DATETIME! END");
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS | MAP_FLG_SUPPR_LA;
  }

  private static final Pattern DELIM = Pattern.compile("\n| \\| ");

  protected boolean parseMsg(String body, Data data) {
    return parseFields(DELIM.split(body), data);
  }

  private static final DateFormat DATE_TIME_FMT = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");

  public Field getField(String name) {
    if (name.equals("ID")) return new IdField("\\d{9}", true);
    if (name.equals("SRC")) return new SourceField("[A-Z]{1,3}[A-Z0-9]", true);
    if (name.equals("ADDR_INFO")) return new MyAddressInfoField();
    if (name.equals("GPS")) return new GPSField("https://.*query=([-+]?\\d{2,3}\\.\\d{3,},[-+]?\\d{2,3}\\.\\d{3,})", true);
    if (name.equals("DATETIME")) return new DateTimeField(DATE_TIME_FMT, true);
    return super.getField(name);
  }

  private class MyAddressInfoField extends AddressField {
    @Override
    public boolean canFail() {
      return true;
    }

    @Override
    public boolean checkParse(String field, Data data) {
      int pt = field.indexOf(": ");
      if (pt > 0) {
        data.strSupp = field.substring(pt+1).trim();
        field = field.substring(0,pt).trim();
      } else if (field.endsWith(":")) {
        field = field.substring(0,field.length()-1).trim();
      } else {
        return false;
      }
      super.parse(field, data);
      return true;
    }

    @Override
    public void parse(String field, Data data) {
      if (!checkParse(field, data)) abort();
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " INFO";
    }
  }
}
