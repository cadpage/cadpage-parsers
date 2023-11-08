package net.anei.cadpage.parsers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.anei.cadpage.parsers.MsgInfo.Data;
import net.anei.cadpage.parsers.MsgInfo.MsgType;

/**
 * Abstract combination parser that accepts the best results of a group of parsers
 */

public class GroupBestParser extends GroupBaseParser {

  // Lists of parsers are mutually compatible.  Which means that there
  // are some calls that they return identical results for.  Since we can
  // not tell which parser returned the results, we have to fudge the
  // default city/count and state in the final result
  private static final String[][] COMPAT_PARSER_LIST = new String[][]{
    {"NCDavidsonCountyA", "NCRowanCounty"}
  };

  private MsgParser[] parsers;

  private String sponsor;

  private Date sponsorDate;

  private SplitMsgOptions splitMsgOptions;

  public GroupBestParser(MsgParser ... parsersP) {

    // Build the final array of parsers.  eliminating parsers that are aliased
    // to another parser in the list.

    // parserListList is a list of lists of message parsers.  First list consists of
    // unblocked parsers.  Second list consists of parsers behind one block, third list
    // consists of parsers behind two blocks, etc...
    List<List<MsgParser>> parserListList = new ArrayList<List<MsgParser>>();

    // Likewise aliasMapList is a list of alias maps for each block level
    List<Map<String, MsgParser>> aliasMapList = new ArrayList<Map<String, MsgParser>>();

    // Add the list of parsers to our accumulated parser lists
    addParsers(parsersP, 0, parserListList, aliasMapList);

    // now merge this list of lists of parser into a single list separated by
    // GroupBlockParsers
    if (parserListList.size() == 0) {
      parsers = new MsgParser[0];
    }
    else {
      List<MsgParser> parserList = parserListList.get(0);
      for (int ndx = 1; ndx < parserListList.size(); ndx++) {
        parserList.add(new GroupBlockParser());
        parserList.addAll(parserListList.get(ndx));
      }
      parsers = parserList.toArray(new MsgParser[parserList.size()]);
    }

    // Group parser is sponsored if all of it subparsers are sponsored
    // If all subparsers are sponsored, sponsor date is the earliest subparser sponsor date
    splitMsgOptions = null;
    sponsor = null;
    sponsorDate = null;
    for (MsgParser parser : parsers) {
      if (splitMsgOptions == null) splitMsgOptions = parser.getActive911SplitMsgOptions();
      String pSponsor = parser.getSponsor();
      if (pSponsor == null) {
        sponsor = null;
        sponsorDate = null;
        break;
      } else {
        Date pDate = parser.getSponsorDate();
        if (pDate == null) {
          if (sponsor == null) sponsor = pSponsor;
        } else {
          if (sponsorDate == null || sponsorDate.after(pDate)) {
            sponsor = pSponsor;
            sponsorDate = pDate;
          }
        }
      }
    }

    // Next we have to make adjustments when there are compatible parsers in the list
    for (String[] list : COMPAT_PARSER_LIST) {

      // First pass checking to see if we have 2 or more parsers from the compatible list
      int cnt = 0;
      String defCity = null;
      String defState = null;
      for (MsgParser parser : parsers) {
        String parserCode = parser.getParserCode();
        for (String code : list) {
          if (parserCode.equals(code)) {
            cnt++;
            defCity = merge(defCity, parser.getDefaultCity());
            defState = merge(defState, parser.getDefaultState());
            break;
          }
        }
      }

      // If we found 2 or more, make another pass replacing the affected
      // parser with an aliased parser that returns the appropriate defaults
      if (cnt >= 2) {
        for (int ndx = 0; ndx<parsers.length; ndx++) {
          MsgParser parser = parsers[ndx];
          String parserCode = parser.getParserCode();
          for (String code : list) {
            if (parserCode.equals(code)) {
              AliasedMsgParser aliasParser;
              if (parser instanceof AliasedMsgParser) {
                aliasParser = (AliasedMsgParser)parser;
              } else {
                parsers[ndx] = aliasParser = new AliasedMsgParser(parser);
              }
              aliasParser.setDefaults(defCity, defState);
              break;
            }
          }
        }
      }
    }
  }

  /**
   * Add list of parser to master parser list(s)
   * @param parsersP list of parsers to be added
   * @param blockLevel number of blocks encountered before this parser list
   * @param parserListList accumulated list of lists of parsers separated by block level
   * @param aliasMap map of alias codes to parsers
   */
  private void addParsers(MsgParser[] parsersP, int blockLevel, List<List<MsgParser>> parserListList, List<Map<String, MsgParser>> aliasMapList) {

    // Run through the list of parsers
    for (MsgParser parser : parsersP) {

      // If we encounter a GropuBlockParsr, it does not get added, but we increment the block level
      if (parser instanceof GroupBlockParser) blockLevel++;

      // If parser is another GroupBestParser, call ourselves recursivelly to process it's parsers
      else if (parser instanceof GroupBestParser) {
        addParsers(((GroupBestParser)parser).parsers, blockLevel, parserListList, aliasMapList);
      }

      // Otherwise just add the parser to the current parser list
      else {
        while (blockLevel+1 > parserListList.size()) {
          parserListList.add(new ArrayList<MsgParser>());
          aliasMapList.add(new HashMap<String, MsgParser>());
        }
        addParser(parser, parserListList.get(blockLevel), aliasMapList.get(blockLevel));
      }
    }
  }

  private void addParser(MsgParser parser, List<MsgParser> parserList,
      Map<String, MsgParser> aliasMap) {
    // Merge the default city/state and filter information.  None of these
    // are really used for any maping or filtering, but they do end up
    // calculating the displayed location name and sender filter.
    addParser(parser);

    // See if this parser has an alias code
    String aliasCode = parser.getAliasCode();
    if (aliasCode != null) {

      // Yep, see if we have already processed a parser with the same alias code
      MsgParser mainParser = aliasMap.get(aliasCode);
      if (mainParser != null) {

        // Yes again.  The main parser is going to replace the aliased parser
        // First step is to make sure the  main parser is an AliasedMsgParser
        // that we can adjust to include things that may differ between aliased
        // parsers
        AliasedMsgParser aliasParser;
        if (mainParser instanceof AliasedMsgParser) {
          aliasParser = (AliasedMsgParser)mainParser;
        } else {
          aliasParser = new AliasedMsgParser(mainParser);
          aliasMap.put(aliasCode, aliasParser);
          int ndx = parserList.indexOf(mainParser);
          parserList.set(ndx, aliasParser);
        }

        // Now that that is taken care of, just add the new parser
        // to the aliased one
        aliasParser.addMsgParser(parser);
        parser = null;
      }

      // No, we do not yet have another parser with this alias code.  So we keep this
      // parser, but add it to the alias map in case another parser is aliased to this one
      else {
        aliasMap.put(aliasCode, parser);
      }
    }

    // If we haven't dropped this parser because it is aliased to another one,
    // add it to this list
    if (parser != null) parserList.add(parser);
  }

  private String merge(String oldDefault, String newDefault) {
    if (oldDefault == null) return newDefault;
    if (oldDefault.equals(newDefault)) return oldDefault;
    return "";
  }

  @Override
  public void setTestMode(boolean testMode) {
    // Propogate the test mode status to all subparsers
    super.setTestMode(testMode);
    for (MsgParser parser : parsers) parser.setTestMode(testMode);
  }

  /**
   * @return list of component subparsers
   */
  MsgParser[] getParsers() {
    return parsers;
  }

  @Override
  protected Data parseMsg(Message msg, int parserFlags) {

    int bestScore = Integer.MIN_VALUE;
    Data bestData = null;

    for (MsgParser parser : parsers) {

      // If we encounter a GroupBlockParser in the list, see if
      // we have found anything so far.  If we have, return it
      if (parser instanceof GroupBlockParser) {
        if (bestData != null && bestData.msgType != MsgType.GEN_ALERT) return bestData;
      }

      // Otherwise invoke this parser and see what kind of result it returns.
      else {
        Data tmp = parser.parseMsg(msg, parserFlags);
        if (tmp != null) {
          // Score the result.
          // We want to seriously ding any result produced by any of the "General" parsers
          // including General Alert.  GENERAL ALERT type messages produced by location
          // parsers do not suffer this penalty
          int newScore = new MsgInfo(tmp).score();
          if (!tmp.parserCode.startsWith("General")) newScore += 100000;
          if (newScore > bestScore) {
            bestData = tmp;
            bestScore = newScore;
          }
        }
      }
    }

    return bestData;
  }

  // We have to override this to satisfy abstract requirements, but it will
  // never be called and doesn't have to do anything
  @Override
  protected boolean parseMsg(String strMessage, Data data) {
    return false;
  }

  @Override
  public String getSponsor() {
    return sponsor;
  }

  @Override
  public Date getSponsorDate() {
    return sponsorDate;
  }

  @Override
  public SplitMsgOptions getActive911SplitMsgOptions() {
    return splitMsgOptions;
  }
}
