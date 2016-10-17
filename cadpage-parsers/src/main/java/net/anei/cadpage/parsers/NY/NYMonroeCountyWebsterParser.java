package net.anei.cadpage.parsers.NY;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.CodeTable;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.StandardCodeTable;
import net.anei.cadpage.parsers.dispatch.DispatchA7BaseParser;

/**
 * Monroe County, NY (Webster) 
 */
public class NYMonroeCountyWebsterParser extends DispatchA7BaseParser {
  
  private static final Pattern MARKER = Pattern.compile("^([A-Z]{4}) +B:([ 0-9A-Z]{4})? +(\\d[A-Z]?|E) +");
  
  public NYMonroeCountyWebsterParser() {
    super(CITY_CODES, "MONROE COUNTY", "NY",
          "BOX:BOX? L:ADDR! BOX:BOX? T:CALL! O:NAME? B:PLACE? PH:PHONE? C1:X? C2:X? X:INFO!");
  }
  
  @Override
  public String getFilter() {
    return "@rednmxcad.com,messaging@iamresponding.com,pagerchanges@monroecounty.gov";
  }

  @Override
  protected boolean parseMsg(String body, Data data) {
    int pt = body.indexOf("\n\n");
    if (pt >= 0) body = body.substring(0,pt).trim();
    body = body.replace('\n', ' ').trim();
    Matcher match = MARKER.matcher(body);
    if (match.find()) {
      data.strSource = match.group(1);
      data.strBox = getOptGroup(match.group(2));
      data.strPriority = match.group(3);
      body = body.substring(match.end()).trim();
    }
    if (!body.startsWith("L:") && !body.startsWith("A:") && !body.startsWith("BOX:")) body = "L:" + body;
    if (!super.parseMsg(body, data)) return false;
    if (data.strSupp.startsWith(",")) data.strSupp = data.strSupp.substring(1).trim();
    return true;
  }
  
  @Override
  public String adjustMapAddress(String addr) {
    // PK is abbreviation of PARK instead of the expected PIKE
    addr = PK_PATTERN.matcher(addr).replaceAll("PARK");
    addr = SPENCERPRT_PTN.matcher(addr).replaceAll("SPENCERPORT");
    addr = OGDENPARMATL_PTN.matcher(addr).replaceAll("OGDEN PARMA TOWN LINE");
    addr = RI_PTN.matcher(addr).replaceAll("RISE");
    addr = XG_PTN.matcher(addr).replaceAll("XING");
    return addr;
  }
  private static final Pattern PK_PATTERN = Pattern.compile("\\bPK\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern SPENCERPRT_PTN = Pattern.compile("\\bSPENCERPRT\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern OGDENPARMATL_PTN = Pattern.compile("\\bOGDEN PARMA T ?L\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern RI_PTN = Pattern.compile("\\bRI\\b", Pattern.CASE_INSENSITIVE);
  private static final Pattern XG_PTN = Pattern.compile("\\bXG\\b", Pattern.CASE_INSENSITIVE);
  
  @Override
  public String getProgram() {
    return "SRC BOX PRI " + super.getProgram();
  }
  
  @Override
  public Field getField(String name) {
    if (name.equals("CALL")) return new MyCallField();
    return super.getField(name);
  }
  
  private class MyCallField extends CallField {
    @Override
    public void parse(String field, Data data) {
      String call = CALL_CODES.getCodeDescription(field);
      if (call != null) {
        data.strCode = field;
        data.strCall = call;
      } else {
        data.strCall = field;
      }
    }
    
    @Override
    public String getFieldNames() {
      return "CODE CALL";
    }
  }
  
  private static final CodeTable CALL_CODES = new StandardCodeTable();
  
  private static final Properties CITY_CODES = buildCodeTable(new String[]{
      
      "AVO", "AVON",
      "BAT", "BATAVIA",
      "BER", "BERGEN",
      "BRI", "BRIGHTON",
      "BRO", "BROCKPORT",
      "BRS", "BRISTOL",
      "CAL", "CALEDONIA",
      "CALE","CALEDONIA",
      "CHI", "CHILI",
      "CHU", "CHURCHVILLE",
      "CLA", "CLARKSON",
      "CLR", "CLARENDON",
      "ERO", "EAST ROCHESTER",
      "FAI", "FAIRPORT",
      "FAR", "FARMINGTON",
      "FRP", "FAIRPORT",
      "GAT", "GATES",
      "GRE", "GREECE",
      "HAM", "HAMLIN",
      "HEN", "HENRIETTA",
      "HFL", "HONEOYE FALLS",
      "HIL", "HILTON",
      "HOL", "HOLEY",
      "HON", "HONEOYE FALLS",
      "IRO", "IRONDEQUOIT",
      "KEN", "KENDALL",
      "LER", "LEROY",
      "LIM", "LIMA",
      "LIV", "LIVONIA",
      "LYN", "LYONS",
      "MAC", "MACEDON",
      "MAR", "MARION",
      "MED", "MEDINA",
      "MEN", "MENDON",
      "MUR", "MURRAY",
      "NEW", "NEWARK",
      "OGD", "OGDEN",
      "ONT", "ONTARIO",
      "PAL", "PALMYRA",
      "PAR", "PARMA",
      "PEN", "PENFIELD",
      "PE",  "PERINTON",
      "PER", "PERINTON",
      "PIT", "PITTSFORD",
      "PIV", "PITTSFORD",
      "RIG", "RIGA",
      "ROC", "ROCHESTER",
      "RUS", "RUSH",
      "SAV", "SAVANNAH",
      "SCO", "SCOTTSVILLE",
      "SOD", "SODUS",
      "SPE", "SPENCERPORT",
      "SWE", "SWEDEN",
      "VIC", "VICTOR",
      "WAL", "WALWORTH",
      "WBL", "WEST BLOOMFIELD",
      "WBT", "WEBSTER",
      "WBV", "WEBSTER",
      "WEB", "WEBSTER",
      "WHE", "WHEATLAND",
      "WLM", "WILLIAMSON",
      
      // Livingston County
      "CALE", "CALEDONIA",
      
      // Ontario County
      "ION", "IONIA"
  });
  
}
	