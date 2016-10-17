package net.anei.cadpage.parsers.PA;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgParser;


public class PAMontgomeryCountyDParser extends MsgParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("^---+ Clear Report ");

  public PAMontgomeryCountyDParser() {
    super("MONTGOMERY COUNTY", "PA");
    setFieldList("ID DATE TIME CODE CALL PLACE ADDR APT X MAP BOX CITY UNIT");
  }
  
  @Override
  public String getFilter() {
    return "unionfa@fdcms.info";
  }

  @Override
  protected boolean parseMsg(String subject, String body, Data data) {
    // Don't know what the deal is with random back quotes??
    body = body.replace('`', ' ');
    body = body.replace('\t', ' ');
    
    if (!body.startsWith("------ Dispatch Report ")) {
      if (!RUN_REPORT_PTN.matcher(body).find()) return false;
      data.strCall = "RUN REPORT";
      data.strPlace = body;
      return true;
    }
    Parser p = new Parser(body);
    Matcher match = p.getMatcher(CALL_ID_PTN);
    if (match == null) return false;
    data.strCallId = match.group(1);
    
    match = p.getMatcher(DATE_TIME_PTN);
    if (match == null) return false;
    data.strDate = match.group(1);
    data.strTime = match.group(2);
    
    match = p .getMatcher(ADDR_INFO_PTN);
    if (match == null) return false;
    data.strCode = getOptGroup(match.group(1));
    data.strCall = match.group(2).trim();
    data.strPlace = match.group(3).trim();
    String sAddr = match.group(4).trim();
    String sCross = match.group(5).trim();
    if (sAddr.length() == 0) {
      sAddr = sCross;
      sCross = "";
    }
    parseAddress(sAddr, data);
    data.strCross = sCross;
    
    match = p.getMatcher(MAP_BOX_CITY_PTN);
    if (match == null) return false;
    data.strMap = match.group(1).trim();
    data.strBox = match.group(2).trim();
    data.strCity = convertCodes(match.group(3).trim(), PAMontgomeryCountyParser.CITY_CODES);

    match = p.getMatcher(UNIT_PTN);
    if (match == null) return false;
    data.strUnit = match.group(1).trim().replaceAll("  +", " ");
    
    return true;
  }
  
  private static final Pattern CALL_ID_PTN = Pattern.compile(" INCIDENT: ([^ ]+) *(?=\n)");
  private static final Pattern DATE_TIME_PTN = Pattern.compile("\nIncident Received: +(\\d\\d-\\d\\d-\\d{4}) +(\\d\\d:\\d\\d:\\d\\d) +Call Source: +.*(?=\n)");
  private static final Pattern ADDR_INFO_PTN = Pattern.compile("\n *CODE: (?:([A-Z0-9]+)-)?(.*)\n(.*)\n(.*)\n *Cross Street: *(.*)(?=\n)");
  private static final Pattern MAP_BOX_CITY_PTN = Pattern.compile("\nMap: +(.*?) +Plan: +.*? +ADC: +(.*?) +MUN: *([A-Z]*) *\n");
  private static final Pattern UNIT_PTN = Pattern.compile("\n *Units Due: *(.*)(?=\n)");
}
	