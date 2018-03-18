package net.anei.cadpage.parsers.CO;

import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

/**
 * Jefferson County, CO
 */
public class COJeffersonCountyAParser extends FieldProgramParser {
  
  private static final Pattern RUN_REPORT_PTN = Pattern.compile("Alarm .*DISP:.*SCN:.*CLR:.*");
  
  private static final Pattern MISSING_COLON_PTN = Pattern.compile("(?<= Pg)(?!:)");
  private static final Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(?=Add:|Apt:|Type:|Pg:|Cross:|Units:|Radio:|Case ?#:)");
  

  public COJeffersonCountyAParser() {
    super("Jefferson County", "CO",
        "Add:ADDR! Apt:APT? Bldg:APT? Loc:PLACE? Type:CALL! Pg:MAP! Cross:X? Units:UNIT? Radio:CH? Case_#:ID!");
   }

  @Override
  public String getFilter() {
    return "@c-msg.net,@westmetrofire.org,messaging@iamresponding.com";
  }
  
  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return new SplitMsgOptionsCustom(){
      @Override public boolean splitBlankIns() { return false; }
      @Override public boolean revMsgOrder() { return true; }
      @Override public boolean mixedMsgOrder() { return true; }
      @Override public int splitBreakLength() { return 135; }
      @Override public int splitBreakPad() { return 1; }
    };
  }

  @Override 
  public boolean parseMsg(String subject, String body, Data data) {
    
    int pt = body.indexOf("\n\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    
    if (!subject.equals("CAD Information")) data.strSource = subject;
    
    if (RUN_REPORT_PTN.matcher(body).matches()) {
      setFieldList("INFO");
      data.msgType = MsgType.RUN_REPORT;
      data.strSupp = body;
      return true;
    }
    
    body = body.replace("Map Pg", " Pg:").replace("Alarm#", " Case #:");
    body = body.replace("Case#;", "Case #:");
    body = MISSING_COLON_PTN.matcher(body).replaceAll(":");
    body = MISSING_BLANK_PTN.matcher(body).replaceAll(" ");
    body = body.replace(" Case#:", " Case #:");
    
    if (!super.parseMsg(body, data)) return false;
    data.strMap = stripFieldEnd(data.strMap, "-");
    data.strCross = data.strCross.replace("no x street avail", "");
    data.strCross = stripFieldStart(data.strCross, "/");
    data.strCross = stripFieldEnd(data.strCross, "/");
    return true;
  }
  
  @Override
  public String getProgram() {
    return "SRC " + super.getProgram() + " PLACE";
  }
}
  





