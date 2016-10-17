package net.anei.cadpage.parsers.VA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.MsgInfo.Data;

public class VAUniversityOfVirginiaParser extends MsgParser {

  private static final String GPS_PATTERN_STRING =
      "(?:(?:GPS: +[A-Z][A-Z0-9]*|LT\\:) +(.+))?$";
  private static final Pattern RESPOND_PATTERN = 
      Pattern.compile("(RESPOND|STANDBY) +(.+?)"+GPS_PATTERN_STRING);
  private static final Pattern CALL_PATTERN =
      Pattern.compile("CALL +(TO STATION|FOR) +(.*?)\\*\\*(.*)");
  private static final Pattern TRAUMA_PATTERN =
      Pattern.compile("(.*?TRAUMA)(.*)");
  private static final Pattern DOUBLE_SPLAT_PATTERN =
      Pattern.compile("^(.*?)\\*\\*(.*)");
  private static final Pattern CALL_ID_PATTERN =
      Pattern.compile("(.*)\\((\\d{4})\\).*");
  private static final Pattern AZIMUTH_RANGE_PATTERN =
      Pattern.compile(".*(\\d{3}(?:DG |\\-)\\d{1,3}nm)(.*)");
  
  public VAUniversityOfVirginiaParser() {
    super("CHARLOTTESVILLE", "VA");
    setFieldList("ADDR CALL ID PLACE INFO");
  }
  
  

  @Override
  public String getLocName() {
    return "University of Virginia, VA";
  }



  @Override
  protected boolean parseMsg(String body, Data data) {
    String dataField;
    if (!isPositiveId()) return false;
  
    Matcher m = RESPOND_PATTERN.matcher(body);
    if (m.matches()) {
      data.strCall = m.group(1).trim();
      dataField = m.group(2).trim();
      data.strAddress = getOptGroup(m.group(3)).replace("LG:", " ").trim().replace("-", " ");
      m = CALL_ID_PATTERN.matcher(dataField);
      if (m.matches()) {
        data.strCall = append(data.strCall, " ", m.group(1).trim());
        data.strCallId = m.group(2);
      }
      m = AZIMUTH_RANGE_PATTERN.matcher(dataField);
      if (m.matches()) {
        data.strPlace = m.group(1);
        dataField = m.group(2).trim();
      }
      String tmp = dataField.startsWith(",") ? "" : " ";
      data.strCall = append(data.strCall, tmp, dataField);
    }
    else {
      m = CALL_PATTERN.matcher(body);
      if (m.matches()) {
        data.strCall = m.group(2).trim();
        data.strSupp = append(data.strSupp, " ", body);
      }
      else {
        m = TRAUMA_PATTERN.matcher(body);
        if (m.matches()) {
          data.strCall = m.group(1);
          data.strSupp = m.group(2).trim();
        }
        else {
          m = DOUBLE_SPLAT_PATTERN.matcher(body);
          if (m.matches()) {
            data.strCall = m.group(1).trim();
            data.strSupp = m.group(2).trim();
          }
          else
            data.strSupp = body;
        }
      }
    }
    return true;
  }

  @Override
  public int getMapFlags() {
    return MAP_FLG_PREFER_GPS;
  }
}
