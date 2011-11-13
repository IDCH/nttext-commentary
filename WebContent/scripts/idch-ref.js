// START WRAPPER: The YUI.add wrapper is added by the build system, when you use 
// YUI Builder to build your component from the raw source in this file

YUI.add("idch-ref", function(Y) {
	
	/* Any frequently used shortcuts, strings and constants */
	var Lang = Y.Lang;
	
	//====================================================================================
    // BOOK NAME
    //====================================================================================
	
	function BookName(osisId, name, alternateSpellings) {
        this.osisId = osisId;
        this.name = name; 
        this.alternateSpellings = alternateSpellings;
    }
  
	//====================================================================================
    // BOOK ORDER
    //====================================================================================
    function BookOrder() {
        this.books = [];
    }
    
    BookOrder.prototype.indexOf = function(osisId) {
    	for (var i = 0; i < this.books.length; i++) {
    		if (this.books[i].osisId == osisId)
    			return i;
    	}
    	
    	return -1;
    };
    
    BookOrder.prototype.getBookName = function(search) {
    	if (Lang.isString(search)) {
    		search = this.indexOf(osisId);
    	}
    	
    	return ((search >= 0) && (search < this.books.length))
    		? this.books[search]
    		: null;
    };
    
    
    BookOrder.prototype.addBook = function(bookName) {
        this.books.push(bookName);
    };
    
    //====================================================================================
    // PASSAGE
    //====================================================================================
    
    
    /**
     * @param bookOrder { BookOrder } The book order to use to look up book names.
     * @param ref { string } The OSIS formatted reference for this passage. This should be
     * 		in the form <tt>1Pet.2.20</tt>.
     */
    function VerseRef(bookOrder, ref) {
    	this.bookOrder = bookOrder;
    	
    	this.index = -1;
    	this.book = null;
    	this.chapter = null;
    	this.verse = null;
    	
    	if (Lang.isString(ref)) {
    		var parts = ref.split(".");
    		this.setBook(parts[0]);
    		if (parts[1]) 
    			this.setChapter(parts[1]);
    		if (parts[2]) 
    			this.setVerse(parts[2]);
    	}
    }
    
    VerseRef.prototype.setBook = function(book) {
    	var order = this.bookOrder;
    	if (!book) {
    		this.book = null;
    	} else {
    		this.index = order.indexOf(book);
    		this.book = order.getBookName(this.index);
    	}
    };
    
    VerseRef.prototype.setChapter = function(ch) {
    	if (Lang.isString(ch)) {
    		ch = parseInt(ch);
    	} 
    	
    	if (Lang.isNumber(ch))
    		this.chapter = ch;
    };
    
    VerseRef.prototype.setVerse = function(vs) {
    	if (Lang.isString(vs)) {
    		this.verse = parseInt(vs);
    	} else if (Lang.isNumber(vs))
    		this.verse = vs;
    };
    
    VerseRef.prototype.format = function() {
    	var str = this.book.name;
    		ch = this.chapter, vs = this.verse;
    	if (ch) {
    		str += " " + ch + ((vs) ? ":" + vs : "");
    	}
    	
    	return str;
    };
    
    VerseRef.prototype.toOsisString = function() {
    	var osis = this.book.osisId,
    	    ch = this.chapter, vs = this.verse;
    	if (ch) {
    		osis += "." + ch + ((vs) ? "." + vs : "");
    	}
    	
    	return osis;
    };
    
    VerseRef.prototype.toString = function() {
    	return this.format();
    };
    
    //====================================================================================
    // VerseRange
    //====================================================================================
    
   
    function VerseRange(bookOrder, ref) {
    	if (bookOrder && ref) {
    		var parts = ref.split("-");
    		this.start = new VerseRef(bookOrder, parts[0]);
    		this.end = (parts[1]) ? new VerseRef(bookOrder, parts[1]) : null;
    	}
    } 
    
    VerseRange.prototype.format = function() {
    	var start = this.start,
    		end = this.end,
    		str = start.format();
    	
    	if (end) {
    		if (start.book.index != end.book.index) {
    			str += " - " + end.book.name;
    			if (end.chapter) {
    	    		str += " " + end.chapter;
    	    		if (end.verse) 
    	    			str += ":" + end.verse;
    			}
    		} else if (end.chapter && (start.chapter != end.chapter)) {
    			str += "-" + end.chapter;
	    		if (end.verse) 
	    			str += ":" + end.verse;
	    		
    		} else if (end.verse && (start.verse != end.verse)) {
    			str += "-" + end.verse;
    		}
    	}
    	
    	return str;
    };
    
    VerseRange.prototype.toOsisString = function() {
    	var osis = "", 	
    		start = this.start, 
    		end = this.end;
    	
    	if (start && end) {
    		osis = start.toOsisString() + "-" + end.toOsisString();
    	} else if (start) {
    		osis = start.toOsisString();
    	} else if (end) {
    		osis = end.toOsisString();
    	}
    	
    	return osis;
    };
    
    //====================================================================================
    // REFERENCE SYSTEM
    //====================================================================================
    
    var RefSystem = {
        Bible : {
            NRSVA : new BookOrder()
        }
    };
    
    
    //====================================================================================
    // INITIALIZE THE NRSVA BookOrder
    //====================================================================================
    
    RefSystem.Bible.NRSVA.addBook(new BookName("Gen", "Genesis",            ["Gen", "Ge", "Gn"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Exod", "Exodus",            ["Exo", "Ex", "Exod"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Lev", "Leviticus",          ["Lev", "Le", "Lv"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Num", "Numbers",            ["Num", "Nu", "Nm", "Nb"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Deut", "Deuteronomy",       ["Deut", "Dt"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Josh", "Joshua",            ["Josh", "Jos", "Jsh"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Judg", "Judges",            ["Judg", "Jdg", "Jg", "Jdgs"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Ruth", "Ruth",              ["Rth", "Ru"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Sam", "1 Samuel",          ["1 Sam", "1 Sa", "1Samuel", "1S", "I Sa", "1 Sm", "1Sa", "I Sam", "1Sam", "I Samuel", "1st Samuel", "First Samuel"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("2Sam", "2 Samuel",          ["2 Sam", "2 Sa", "2S", "II Sa", "2 Sm", "2Sa", "II Sam", "2Sam", "II Samuel", "2Samuel", "2nd Samuel", "Second Samuel"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Kgs", "1 Kings",           ["1 Kgs", "1 Ki", "1K", "I Kgs", "1Kgs", "I Ki", "1Ki", "I Kings", "1Kings", "1st Kgs", "1st Kings", "First Kings", "First Kgs", "1Kin"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("2Kgs", "2 Kings",           ["2 Kgs", "2 Ki", "2K", "II Kgs", "2Kgs", "II Ki", "2Ki", "II Kings", "2Kings", "2nd Kgs", "2nd Kings", "Second Kings", "Second Kgs", "2Kin"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Chr", "1 Chronicles",      ["1 Chron", "1 Ch", "I Ch", "1Ch", "1 Chr", "I Chr", "1Chr", "I Chron", "1Chron", "I Chronicles", "1Chronicles", "1st Chronicles", "First Chronicles"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Chr", "2 Chronicles",      ["2 Chron", "2 Ch", "II Ch", "2Ch", "II Chr", "2Chr", "II Chron", "2Chron", "II Chronicles", "2Chronicles", "2nd Chronicles", "Second Chronicles"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Ezra", "Ezra",              ["Ezra", "Ezr"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Neh", "Nehemiah",           ["Neh", "Ne"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Esth", "Esther",            ["Esth", "Es"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Job", "Job",                ["Job", "Job", "Jb"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Ps", "Psalm",               ["Pslm", "Ps", "Psalms", "Psa", "Psm", "Pss"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Prov", "Proverbs",          ["Prov", "Pr", "Prv"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Eccl", "Ecclesiastes",      ["Eccles", "Ec", "Qoh", "Qoheleth"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Song", "Song of Solomon",   ["Song", "So", "Canticle of Canticles", "Canticles", "Song of Songs", "SOS"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Isa", "Isaiah",             ["Isa", "Is"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Jer", "Jeremiah",           ["Jer", "Je", "Jr"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Lam", "Lamentations" ,      ["Lam", "La"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Ezek", "Ezekiel",           ["Ezek", "Eze", "Ezk"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Dan", "Daniel",             ["Dan", "Da", "Dn"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Hos", "Hosea",              ["Hos", "Ho"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Joel", "Joel",              ["Joel", "Joe", "Jl"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Amos", "Amos",              ["Amos", "Am"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Obad", "Obadiah",           ["Obad", "Ob"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Jonah", "Jonah",            ["Jnh", "Jon"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Mic", "Micah",              ["Micah", "Mic"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Nah", "Nahum",              ["Nah", "Na"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Hab", "Habakkuk",           ["Hab", "Hab"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Zeph", "Zephaniah",         ["Zeph", "Zep", "Zp"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Hag", "Haggai",             ["Haggai", "Hag", "Hg"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Zech", "Zechariah",         ["Zech", "Zec", "Zc"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Mal", "Malachi",            ["Mal", "Mal", "Ml"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Matt", "Matthew",           ["Matt", "Mt"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Mark", "Mark",              ["Mrk", "Mk", "Mr"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Luke", "Luke",              ["Luk", "Lk"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("John", "John",              ["John", "Jn", "Jhn"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Acts", "Acts",              ["Acts", "Ac"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Rom", "Romans",             ["Rom", "Ro", "Rm"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Cor", "1 Corinthians",     ["1 Cor", "1 Co", "I Co", "1Co", "I Cor", "1Cor", "I Corinthians", "1Corinthians", "1st Corinthians", "First Corinthians"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("2Cor", "2 Corinthians",     ["2 Cor", "2 Co", "II Co", "2Co", "II Cor", "2Cor", "II Corinthians", "2Corinthians", "2nd Corinthians", "Second Corinthians"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Galatians",                 ["Gal", "Ga"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Eph", "Ephesians",          ["Ephes", "Eph"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Phil", "Philippians",       ["Phil", "Php"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Col", "Colossians",         ["Col", "Col"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Thess", "1 Thessalonians", ["1 Thess", "1 Th", "I Th", "1Th", "I Thes", "1Thes", "I Thess", "1Thess", "I Thessalonians", "1Thessalonians", "1st Thessalonians", "First Thessalonians"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("2Thess", "2 Thessalonians", ["2 Thess", "2 Th", "II Th", "2Th", "II Thes", "2Thes", "II Thess", "2Thess", "II Thessalonians", "2Thessalonians", "2nd Thessalonians", "Second Thessalonians"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Tim", "1 Timothy",         ["1 Tim", "1 Ti", "I Ti", "1Ti", "I Tim", "1Tim", "I Timothy", "1Timothy", "1st Timothy", "First Timothy"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Tim", "2 Timothy",         ["2 Tim", "2 Ti", "II Ti", "2Ti", "II Tim", "2Tim", "II Timothy", "2Timothy", "2nd Timothy", "Second Timothy"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Titus", "Titus",            ["Titus", "Tit"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Phlm", "Philemon",          ["Philem", "Phm"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Heb", "Hebrews",            ["Hebrews", "Heb"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Jas", "James",              ["James", "Jas", "Jm"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1Pet", "1 Peter",           ["1 Pet", "1 Pe", "I Pe", "1Pe", "I Pet", "1Pet", "I Pt", "1 Pt", "1Pt", "I Peter", "1Peter", "1st Peter", "First Peter"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("2Pet", "2 Peter",           ["2 Pet", "2 Pe", "II Pe", "2Pe", "II Pet", "2Pet", "II Pt", "2 Pt", "2Pt", "II Peter", "2Peter", "2nd Peter", "Second Peter"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("1John", "1 John",           ["1 John", "1 Jn", "I Jn", "1Jn", "I Jo", "1Jo", "I Joh", "1Joh", "I Jhn", "1 Jhn", "1Jhn", "I John", "1John", "1st John", "First John"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("2John", "2 John",           ["2 John", "2 Jn", "II Jn", "2Jn", "II Jo", "2Jo", "II Joh", "2Joh", "II Jhn", "2 Jhn", "2Jhn", "II John", "2John", "2nd John", "Second John"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("3John", "3 John",           ["3 John", "3 Jn", "III Jn", "3Jn", "III Jo", "3Jo", "III Joh", "3Joh", "III Jhn", "3 Jhn", "3Jhn", "III John", "3John", "3rd John", "Third John"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Jude","Jude",               ["Jude", "Jud"]));
    RefSystem.Bible.NRSVA.addBook(new BookName("Rev", "Revelation",         ["Rev", "Re", "The Revelation"]));
    
    
    //====================================================================================
    // REFERENCE PARSER CLASS
    //====================================================================================
    
    var CHAPTER_VERSE_PATTERN = /(\d+)(?:\D+(\d+)(?:\s*,\s*(\d+))?)?/;
    
    function ReferenceParser(bookOrder) {
        this.bookOrder = bookOrder;
        this.regExs = {};
    }
    
    ReferenceParser.prototype.getBookOrder = function() {
    	return this.bookOrder;
    }; 
    
    ReferenceParser.prototype.format = function(ref) {
    	var parts = ref.split("-"),
    	    firstRef = parts[0],
    	    secondRef = parts[1];
    	
    	parts = firstRef.split(".");
    };
    
    
    
    ReferenceParser.prototype.parse = function(ref) {
    	var parts = ref.replace(/^\s*|\s*$/g, '').replace(/–|—/g, "-").split(/\s*-+\s*/);
    	
    	
    	var osisRef = "";
        var startBook = null;
        var startChapter = 0;
        var startVerse = 0;
        
        var endBook = null;
        var endChapter = 0;
        var endVerse = 0;
        
    	// parse the starting reference
    	var firstRef = parts[0];
    	for (var bk in this.regExs) {
    		var re = this.regExs[bk];
    		
    		if (re.test(firstRef)) {
    			startBook = bk;
                firstRef = firstRef.replace(re, "");
                
                // parse out chapter
                if (CHAPTER_VERSE_PATTERN.test(firstRef)) {
                	nums = CHAPTER_VERSE_PATTERN.exec(firstRef);
                    startChapter = parseInt(nums[1]);
                    
                    // verse
                    if (nums[2]) {
                        startVerse = parseInt(nums[2]);
                    }
                    
                    // if a comma is separating two verse then honor that
                    if (nums[3]) {
                        endBook = startBook;
                        endChapter = startChapter;
                        endVerse = parseInt(nums[3]);
                        if (endVerse != startVerse + 1)
                            endVerse = 0;
                    }
                }
                
    			break;   // found a book for the first reference
    		}
    	}                // end for loop to parse first reference
    	
    	// build the reference
    	if (!startBook) {
            return "";
        }
        
        osisRef = startBook;
        if (startChapter != 0)
            osisRef += "." + startChapter;
        if (startVerse != 0)
            osisRef += "." + startVerse;
        
        if (parts[1]) {
        	var secondRef = parts[1];
        	// look for the second book name
        	for (var bk in this.regExs) {
                var re = this.regExs[bk];
                
                if (re.test(secondRef)) {
                	endBook = bk;
                	secondRef = secondRef.replace(re, "");
                }
        	}
        	
        	// look for ending chapter and verse
        	if (CHAPTER_VERSE_PATTERN.test(secondRef)) {
        		var nums = CHAPTER_VERSE_PATTERN.exec(secondRef);
        		
        		// if only one number and the ending book isn't specified, this is a verse
        		if (!nums[2] && !endBook) {
        			if (startVerse > 0) {
                        // if a verse is specified in start ref, then the number is a verse
                        endChapter = startChapter;
                        endVerse = parseInt(nums[1]);
                    } else { 
                        // if only the chapter is specified, then the number is a chapter
                        endChapter = parseInt(nums[1]);
                    }
        		} else {
        			// either the book is supplied or a chapter and verse are supplied
                    // in this case, don't adopt anything from the start ref
                    endChapter = parseInt(nums[1]);
                    if (nums[2]) {
                        endVerse = parseInt(nums[2]);
                    }
        		}
        	}      // done looking for ending chapter and verse
        	
        	if (!endBook) {
                endBook = startBook;
            }
        }   // done looking for end reference
        
        if (endBook) {
            osisRef += "-" + endBook;
            if (endChapter > 0) 
                osisRef += "." + endChapter;

            if (endVerse > 0) 
                osisRef += "." + endVerse;
        }
        
        return osisRef;
    };    
    
    ReferenceParser.prototype.addRegEx = function(osisId, regEx) {
        this.regExs[osisId] = regEx;
    };
    
    
    var simpleNTParser = new ReferenceParser(RefSystem.Bible.NRSVA);
    simpleNTParser.addRegEx("1Cor",    /^[1I]\s*C\D*/i); 
    simpleNTParser.addRegEx("1John",   /^[1I]\s*J\D*/i);
    simpleNTParser.addRegEx("1Pet",    /^[1I]\s*P\D*/i);
    simpleNTParser.addRegEx("1Thess",  /^[1I]\s*Th\D*/i);
    simpleNTParser.addRegEx("1Tim",    /^[1I]\s*T[im]\D*/i);
    simpleNTParser.addRegEx("2Cor",    /^(?:2|II)\s*C\D*/i);
    simpleNTParser.addRegEx("2John",   /^(?:2|II)\s*J\D*/i);
    simpleNTParser.addRegEx("2Pet",    /^(?:2|II)\s*P\D*/i); 
    simpleNTParser.addRegEx("2Thess",  /^(?:2|II)\s*Th\D*/i); 
    simpleNTParser.addRegEx("2Tim",    /^(?:2|II)\s*T[im]\D*/i);
    simpleNTParser.addRegEx("3John",   /^(?:3|III)\s*J\D*/i); 
    simpleNTParser.addRegEx("Acts",    /^A\D*/i); 
    simpleNTParser.addRegEx("Col",     /^C\D*/i); 
    simpleNTParser.addRegEx("Eph",     /^E\D*/i); 
    simpleNTParser.addRegEx("Gal",     /^G\D*/i); 
    simpleNTParser.addRegEx("Heb",     /^H\D*/i); 
    simpleNTParser.addRegEx("Jas",     /^J[mas]\D*/i);
    simpleNTParser.addRegEx("John",    /^Jo?[hn]\D*/i);
    simpleNTParser.addRegEx("Jude",    /^Ju\D*/i); 
    simpleNTParser.addRegEx("Luke",    /^L\D*/i); 
    simpleNTParser.addRegEx("Mark",    /^Ma?[rk]\D*/i);
    simpleNTParser.addRegEx("Matt",    /^Ma?t?t\D*/i);
    simpleNTParser.addRegEx("Phlm",    /^Ph\w*m\D*/i);
    simpleNTParser.addRegEx("Phil",    /^Ph(il|\w*p)\D*/i);
    simpleNTParser.addRegEx("Phil",    /^Ph\D*/i);
    simpleNTParser.addRegEx("Rev",     /^R[ev]\D*/i);
    simpleNTParser.addRegEx("Rom",     /^R[om]\D*/i);
    simpleNTParser.addRegEx("Titus",   /^T\D*/i);

    // attach everything to the package hierarchy
    Y.namespace("idch.ref").BookName = BookName;
    Y.namespace("idch.ref").VerseRef = VerseRef;
    Y.namespace("idch.ref").VerseRange = VerseRange;
    Y.namespace("idch.ref").BookOrder = BookOrder;
    Y.namespace("idch.ref").RefSystem = RefSystem;
    Y.namespace("idch.ref").ReferenceParser = ReferenceParser;
    Y.namespace("idch.ref").ReferenceParser.NT = simpleNTParser;
    
}, "3.2.0", {requires:[]});
// END WRAPPER