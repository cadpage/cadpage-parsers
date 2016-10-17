package net.anei.cadpage.parsers.MO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.MsgParser;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class MOBuchananCountyBParser extends MsgParser {
  public MOBuchananCountyBParser() {
    super("BUCHANAN COUNTY", "MO");
  }

  @Override
  public SplitMsgOptionsCustom getActive911SplitMsgOptions() {
	  return new SplitMsgOptionsCustom();
  }
  
  @Override
  public String getFilter() {
    return "APREWITT@heartland-health.com";
  }
  
  private static final String FORMAT_HEADER_S = "[A-Z]+:";
  
  private static final Pattern 
    CONT_PTN = Pattern.compile(" *--CONT-- [A-Z]+:PART \\d+ +"),
    FORMAT_RR = Pattern.compile(FORMAT_HEADER_S+"(\\S+) +#(\\d{6}) +Addr (.+) (RECV (\\d{2}:\\d{2}).*)"),
    RR_TIMES = Pattern.compile(" +(?=ALRT|ENRT|ARRV|DEST|COMP|TM)"),
    FORMAT_1 = Pattern.compile(FORMAT_HEADER_S+"(\\S+) PRI:(\\d+) Addr (.*?) CTY:(.*?) PROB:(.+) (\\d{6})"),
    FORMAT_2 = Pattern.compile(FORMAT_HEADER_S+"Unit:(\\S+) +(-EP-(?: POST)?) +(.*)"),
    FORMAT_3 = Pattern.compile(FORMAT_HEADER_S+"(\\S+) +#(\\d{6}) +P/U: (\\d\\d:\\d\\d) PT:(.*?) PUNM:(.*?) Addr:(.*?) CTY:(.*?) RM:(.*?) (WC:.*)"),
    FORMAT_3_BRK = Pattern.compile(" (?=DSTNM:|DSTAD:|CTY:|RM:)");
  
  @Override
  protected boolean parseMsg(String body, Data data) {
    if (body.endsWith("--CONT--")) {
      data.expectMore = true;
      body = body.substring(0,body.length()-6).trim();
    } 
    else if (body.endsWith("END-OF-MSG") || body.endsWith(" End-of-Msg")) {
      body = body.substring(0,body.length()-10).trim();
    }
    
    body = CONT_PTN.matcher(body).replaceAll(" ");
    
    Matcher m = FORMAT_RR.matcher(body);
    if (m.matches()) {
      data.msgType = MsgType.RUN_REPORT;
      data.strUnit = m.group(1);
      data.strCallId = m.group(2);
      parseAddress(m.group(3).trim(), data);
      data.strSupp = RR_TIMES.matcher(m.group(4)).replaceAll("\n");
      data.strTime = m.group(5);
      setFieldList("UNIT ID ADDR TIME INFO");
      return true;
    }
    
    m = FORMAT_1.matcher(body);
    if (m.matches()) {
      data.strUnit = m.group(1);
      data.strPriority = m.group(2);
      parseFormat1Address(m.group(3).trim(), data);
      data.strCity = m.group(4).trim();
      parseCall(m.group(5).trim(), data);
      data.strCallId = m.group(6);
      setFieldList("UNIT PRI ADDR INFO CITY CODE CALL ID");
      return true;
    }
    
    m = FORMAT_2.matcher(body);
    if (m.matches()) {
      data.strUnit = m.group(1);
      data.strCall = m.group(2);
      parseAddress(m.group(3).trim(), data);
      setFieldList("UNIT CALL ADDR");
      return true;
    }
    
    m = FORMAT_3.matcher(body);
    if (m.matches()) {
      data.strUnit = m.group(1).trim();
      data.strCallId = m.group(2);
      data.strTime = m.group(3);
      data.strName = m.group(4).trim();
      data.strPlace = m.group(5).trim();
      parseAddress(m.group(6).trim(), data);
      data.strCity = m.group(7).trim();
      data.strApt = m.group(8).trim();
      data.strSupp = FORMAT_3_BRK.matcher(m.group(9).trim()).replaceAll("\n");
      setFieldList("UNIT ID TIME NAME PLACE ADDR CITY APT INFO");
      return true;
    }
    return false;
  }
  
  private static final Pattern CALL_PATTERN = Pattern.compile("(\\d+) +(.*)");
  private void parseCall(String field, Data data) {
  	Matcher m = CALL_PATTERN.matcher(field);
  	if (m.matches()) {
  		data.strCode = m.group(1);
  		field = m.group(2);
  	}
  	data.strCall = field;
  }
  
  private static final Pattern FMT1_ADDRESS_PATTERN = Pattern.compile("(.*) (AltA.*)");
  private void parseFormat1Address(String field, Data data) {
    Matcher m = FMT1_ADDRESS_PATTERN.matcher(field);
    if (m.matches()) {
      field = m.group(1).trim();
      data.strSupp = m.group(2);
    }
    parseAddress(field, data);
  }
}
