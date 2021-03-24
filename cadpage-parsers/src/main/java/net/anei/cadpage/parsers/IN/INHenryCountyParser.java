package net.anei.cadpage.parsers.IN;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.FieldProgramParser;

/**
 * Henry County, IN
 */
public class INHenryCountyParser extends FieldProgramParser {

  private static final Pattern DELIM = Pattern.compile("\\|+");

  public INHenryCountyParser() {
    super("HENRY COUNTY", "IN",
          "CALL ADDRCITY! INFO/N+");
  }

  @Override
  public String getFilter() {
    return "hcradio@emgsvcs.net,@henryco911.org,@henrycounty.in.gov";
  }

  @Override
  public boolean parseMsg(String subject, String body, Data data) {

    if (subject.length() > 0) {
      subject = stripFieldStart(subject, "DISPATCH:");
      subject = stripFieldEnd(subject, "(no subject)");
      subject = stripFieldStart(subject, "[");
      subject = stripFieldEnd(subject, "]");
      subject = stripFieldEnd(subject, "riprun");
      data.strSource = subject;
    }

    return parseFields(DELIM.split(body), data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new CallField("[- A-Z]+", true);
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private class MyAddressCityField extends AddressField {

    @Override
    public void parse(String field, Data data) {
      int pt = field.lastIndexOf(':');
      if (pt >= 0) {
        data.strCity = field.substring(pt+1).trim();
        field = field.substring(0, pt).trim();
      }
      super.parse(field,  data);
    }

    @Override
    public String getFieldNames() {
      return super.getFieldNames() + " CITY";
    }
  }

  private static final Pattern INFO_LAT_LON_PTN = Pattern.compile(" *\\b(?:Lat|Lon): *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_LAT_LON_PTN.matcher(field).replaceAll(" ").trim();
      super.parse(field, data);
    }
  }
}
