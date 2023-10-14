package net.anei.cadpage.parsers.ZCABC;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
/**
 * Nanaimo, BC, Canada
 */
public class ZCABCNanaimoParser extends FieldProgramParser {

  public ZCABCNanaimoParser() {
    super("NANAIMO", "BC",
          "Date:DATETIME! Incident_No:ID! Type:CALL! Location:ADDRCITY! Building_Name:PLACE? Map_Grid:MAP? Units_Responding:UNIT? Notes:INFO INFO/N+");
  }

  @Override
  public String getFilter() {
    return "sfsdispatch@surrey.ca";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (!subject.equals("Incident Message")) return false;
    int pt = body.indexOf("\n<FDM_CADMessage>");
    if (pt >= 0) body = body.substring(0,pt).trim();
    if (!parseFields(body.split("\n"),  data)) return false;
    if (data.strCity.equals("GABRIOLA ISLD")) data.strCity = "GABRIOLA";
    return true;
  }

  @Override
  public Field getField(String name) {
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("ADDRCITY")) return new MyAddressCityField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d) (\\d\\d:\\d\\d:\\d\\d)");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }

  private static final Pattern APT_ADDR_PTN = Pattern.compile("# (.*?) - (.*)");
  private class MyAddressCityField extends AddressCityField {
    @Override
    public void parse(String field, Data data) {
      String apt = "";
      Matcher match = APT_ADDR_PTN.matcher(field);
      if (match.matches()) {
        data.strApt =  match.group(1).trim();
        field =  match.group(2).trim();
      }
      super.parse(field, data);
      data.strApt = append(data.strApt, "-", apt);
    }
  }

  private static Pattern INFO_HDR_PTN = Pattern.compile("^\\d{4}-\\d\\d-\\d\\d \\d\\d:\\d\\d:\\d\\d [0-9A-Za-z ]+: *");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      field = INFO_HDR_PTN.matcher(field).replaceFirst("");
      super.parse(field, data);
    }
  }

  @Override
  public String adjustMapAddress(String addr) {
    addr = TRANS_CANADA_PTN.matcher(addr).replaceAll("ISLAND");
    return addr;
  }
  Pattern TRANS_CANADA_PTN = Pattern.compile("\\bTRANS[- ]CANADA\\b", Pattern.CASE_INSENSITIVE);
}
