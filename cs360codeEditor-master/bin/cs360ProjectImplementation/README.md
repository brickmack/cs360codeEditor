# cs360codeEditor

MAJOR CHANGES:

*All classes, methods, and attributes defined in the class diagram are included, though many are not yet functional. Removed/renamed existing ones as necessary to match current names

*Demo text files are now read in, not hardcoded (but address is hardcoded still)

*Removed most boilerplate code

*Added find/replace dialog box, but it has no functionality yet

*Language supports reading in some of its attributes from a configuration file. This is NOT to be used for actual highlighting yet (use the full constructor), only testing (but it looks to work pretty well so hopefully it can be fully implemented and used operationally soon)

*Added right click menu to the tab bar (currently does nothing, will add close all and other features later)

*Added line numbering (and eventually fold/unfold button) class


KNOWN BUGS:

*Redo does not work at all

*Undo is broken if any tab is closed

*Nested comments and/or strings behave incorrectly (string inside a comment shows as blue instead of green)


FURTHER WORK:

*File save and save as (Aaron)

*Tab rename (Aaron)

*Add find/replace functionality (Austin)

*Fix issues with undo/redo (who?)

*Keyboard shortcuts (Aaron)

*Detect variable declarations (Mack)

*Code insertion (Aaron)

*Add code folding buttons (Mack) and logic (who?)

*Move Language instantiation to TabWindow from Tab (Mack)

*Complete definition for Java (Mack)

*Create definition and test file for C (Mack)
