package net.anei.cadpage.parsers.MN;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class MNHennepinCountyBParser extends FieldProgramParser {

  public MNHennepinCountyBParser() {
    super("HENNEPIN COUNTY", "MN",
          "Nature:CALL! CALL/CS+ Addr:ADDR! City:CITY! ST_ZIP! CAD:ID! Date/Time:DATETIME! Lat/Long:GPS! Cross:X! Priority:PRI! Jur:MAP! Apt:APT! " +
            "RPName:NAME! NAME/CS+ RPPhone:PHONE! RPAdd:SKIP! Comments:INFO END");
  }

  @Override
  public String getFilter() {
    return "noreply_CSTCad2Cad@csqr.cloud";
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }

  private static final Pattern SUBJECT_PTN = Pattern.compile("(.*?) +(?:Notify|Incident Created)");
  private static final Pattern DELIM = Pattern.compile(",\\s+");

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    if (subject.isEmpty()) return false;

    Matcher match = SUBJECT_PTN.matcher(subject);
    if (match.matches()) {
      data.strSource = match.group(1);
    }

    String[] flds;
    int pt = body.indexOf(", Comments:");
    if (pt >= 0) {
      String[] tflds = DELIM.split(body.substring(0,pt).trim());
      flds = new String[tflds.length+1];
      System.arraycopy(tflds, 0, flds, 0, tflds.length);
      flds[tflds.length] = body.substring(pt+2);
    } else if (body.endsWith(", No Comments")) {
      body = body.substring(0, body.length()-13).trim();
      flds = DELIM.split(body);
    } else {
      return false;
    }

    return parseFields(flds, data);
  }

  @Override
  public String getProgram() {
    return "SRC " + super.getProgram();
  }

  @Override
  public Field getField(String name) {
    if (name.equals("ST_ZIP")) return new MyStateZipField();
    if (name.equals("DATETIME")) return new MyDateTimeField();
    if (name.equals("INFO")) return new MyInfoField();
    return super.getField(name);
  }

  private static final Pattern ST_ZIP_PTN = Pattern.compile("([A-Z]{2})(?: +\\d{5})?");

  private class MyStateZipField extends Field {
    @Override
    public void parse(String field, Data data) {
      Matcher match = ST_ZIP_PTN.matcher(field);
      if (match.matches()) field = match.group(1);
    }

    @Override
    public String getFieldNames() {
      return "ST";
    }
  }

  private static final Pattern DATE_TIME_PTN = Pattern.compile("(\\d{4})-(\\d\\d)-(\\d\\d)T(\\d\\d:\\d\\d:\\d\\d) C[DS]T");
  private class MyDateTimeField extends DateTimeField {
    @Override
    public void parse(String field, Data data) {
      Matcher match = DATE_TIME_PTN.matcher(field);
      if (!match.matches()) abort();
      data.strDate = match.group(2)+'/'+match.group(3)+'/'+match.group(1);
      data.strTime = match.group(4);
    }
  }

  private static final Pattern INFO_HDR_PTN = Pattern.compile("^\\d\\d/\\d\\d/\\d{4} \\d\\d:\\d\\d:\\d\\d [AP]M +");
  private class MyInfoField extends InfoField {
    @Override
    public void parse(String field, Data data) {
      for (String line : field.split("\n")) {
        line = line.trim();
        line = INFO_HDR_PTN.matcher(line).replaceFirst("");
        data.strSupp = append(data.strSupp, "\n", line);
      }
    }
  }
}
