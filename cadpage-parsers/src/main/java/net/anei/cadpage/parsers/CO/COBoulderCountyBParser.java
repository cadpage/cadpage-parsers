 package net.anei.cadpage.parsers.CO;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.anei.cadpage.parsers.FieldProgramParser;
import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.SplitMsgOptions;
import net.anei.cadpage.parsers.SplitMsgOptionsCustom;

public class COBoulderCountyBParser extends FieldProgramParser {

	public COBoulderCountyBParser() {
		super("BOULDER COUNTY", "CO",
		      "( Comment:INFO EMPTY? ( CAD:ID! CALL:CALL! LOC:PLACE! ADD:ADDR! CITY:CITY! INFO:INFO/N! UNIT:UNIT! " +
                                "| Inc#:ID! EMPTY? ( Add:ADDR! Problem:CALL! INFO:INFO! " +
      		                                        "| Type:CALL! Key_Details:INFO! Addr:ADDR! Apt:APT! City:CITY! Unit:UNIT! " +
      		                                        ") " +
      		                      "| Call_Type:CALL! EMPTY? ( Call_Address:ADDR! | Address:CALL! ) Location_Name:PLACE! INFO2/N+ " +
      		                      "| END ) " +
		      "| ADDR:ADDR APT:APT? PROB:PROB! UNITS:UNIT! Map_Page:MAP! Assigned_Units:UNIT " +
	        "| CALL! ADD:ADDR! BLD:APT! APT:APT! LOC:PLACE! INFO:INFO! TIME:TIME! UNITS:UNIT% EMD:CODE ZIP:ZIP TAC:CH )");
	}

	@Override
	public String getFilter() {
	  return "bretsa@bretsaps.org";
	}

	@Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
	  return new SplitMsgOptionsCustom(){
	    @Override public boolean splitBlankIns() { return false; }
	    @Override public boolean mixedMsgOrder() { return true; }
	    @Override public int splitBreakLength() { return 150; }
	  };
  }

	private static Pattern MARKER = Pattern.compile("\\*+ ?(?:ADVISORY NOTIFICATION|NOTIFICATION PAGE) ?\\*+");
	private static Pattern DELIM2 = Pattern.compile(" *(?=(?:CAD|CALL|LOC|ADD|CITY|INFO|UNIT|Inc#|Add|Problem|(?<!Call )Type|Key Details|Addr|Apt|City|Unit|Call Type|Call Address|(?<!Call )Address|Location Name):)|\\[1\\]");
  private static Pattern ID_PTN = Pattern.compile("([A-Z]{3,4}\\d{6}-\\d{6}) +(.*)");
	private static Pattern MISSING_BLANK_PTN = Pattern.compile("(?<! )(ADD|APT|BLD|INFO|LOC|PROB|TIME|UNITS|Map Page):");
  private static Pattern SRC_ID_PTN = Pattern.compile("([A-Z]{3,4})\\d{6}-\\d{6}");

	public boolean parseMsg(String body, Data data) {

	  Matcher match = MARKER.matcher(body);
    if (match.lookingAt()) {
	    body = "Comment: " + body.substring(match.end());
	  } else if (body.startsWith("Comment:")) {
	    body = match.replaceFirst("");
	  }

	  if (body.startsWith("Comment:")) {
	    body = body.replace("Inc #", "Inc#:");
	    if (!parseFields(DELIM2.split(body), data)) return false;
	  }

	  else {
  	  match = ID_PTN.matcher(body);
  	  if (!match.matches()) return false;
  	  data.strCallId = match.group(1);
  	  body = match.group(2);

  	  body = body.replace("Response:", "UNITS:");
  	  body = MISSING_BLANK_PTN.matcher(body).replaceAll(" $1:");

  	  if (!super.parseMsg(body, data)) return false;
	  }
	  match = SRC_ID_PTN.matcher(data.strCallId);
	  if (match.matches()) data.strSource = match.group(1);
	  return true;
	}

  @Override
  public String getProgram() {
    return "SRC ID "+super.getProgram();
  }

	@Override
  public Field getField(String name) {
    if (name.equals("CITY")) return new MyCityField();
    if (name.equals("PROB")) return new MyProbField();
    if (name.equals("TIME")) return new TimeField("\\d\\d:\\d\\d");
    if (name.equals("INFO2")) return new MyInfo2Field();
    return super.getField(name);
  }

	private class MyCityField extends CityField {
	  @Override
	  public void parse(String field, Data data) {
	    field = stripFieldEnd(field, " U");
	    super.parse(field, data);
	  }
	}

	private static Pattern PROB = Pattern.compile("(.*) \\((L\\d)\\)(?: ([\\dA-Z]+))?");
	private class MyProbField extends CallField {
    @Override
    public void parse(String field, Data data) {
      Matcher mat = PROB.matcher(field);
      if (!mat.matches()) super.parse(field, data);
      else {
        data.strCall = mat.group(1).trim();
        data.strPriority = mat.group(2);
        data.strCode = getOptGroup(mat.group(3));
      }
    }

    @Override
    public String getFieldNames() {
      return "CALL PRI CODE";
    }
  }

	private static final Pattern INFO_BRK_PTN = Pattern.compile(" *\\[\\d\\] *");
	private class MyInfo2Field extends InfoField {
	  @Override
	  public void parse(String field, Data data) {
	    for (String line : INFO_BRK_PTN.split(field)) {
	      data.strSupp = append(data.strSupp, "\n", line);
	    }
	  }
	}

	@Override
	public String adjustMapCity(String city) {
	  if (city.equals("CU")) return "BOULDER";
	  return city;
	}
}
