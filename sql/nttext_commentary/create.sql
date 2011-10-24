-- A Commentary Entry
CREATE TABLE IF NOT EXISTS NTTEXTComm_Entries (
    entry_id     SERIAL  PRIMARY KEY,  
    -- The scripture passage this entry refers to
    passage      VARCHAR(64) NOT NULL, 
    -- A overview of the variants found in this passage 
    overview     TEXT,                 
    -- The username of the user who owns this entry This implies that usernames should not 
    -- be modified. Otherwise, we could use id, possibly UUID. 
    owned_by     VARCHAR(64),          
    status       VARCHAR(64),
    
    date_created TIMESTAMP DEFAULT now(),
    last_updated TIMESTAMP NOT NULL
    
) ENGINE=InnoDB CHARSET utf8;


-- Variation Units
CREATE TABLE IF NOT EXISTS NTTEXTComm_VUs (
    vu_id        SERIAL PRIMARY KEY,
    passage      VARCHAR(64) NOT NULL,
    commentary   TEXT,
    
    date_created TIMESTAMP DEFAULT now(),
    last_updated TIMESTAMP NOT NULL
) ENGINE=InnoDB CHARSET utf8;

-- Links a VU to its manifestation in a particular NT edition.
-- TODO remove - this should simply be a structure . . . . maybe
CREATE TABLE IF NOT EXISTS NTTEXTComm_VUReference (
    vu_id           BIGINT UNSIGNED NOT NULL, 
    structure_uuid  CHAR(38) NOT NULL,
    baseReading     TEXT NOT NULL,

    FOREIGN KEY (vu_id)
      REFERENCES NTTEXTComm_VUs (vu_id)
      ON DELETE CASCADE
) ENGINE=InnoDB CHARSET utf8;

-- Variant Readings
CREATE TABLE IF NOT EXISTS NTTEXTComm_Rdgs (
    -- Unique id for this reading
    rdg_id          SERIAL PRIMARY KEY,         
    -- The variation unit this is a reading of 
    vu_id           BIGINT UNSIGNED NOT NULL,   
    -- Greek form of this variant reading 
    greek_rdg       TEXT NOT NULL,              
    -- English language gloss of this reading
    english_rdg     TEXT NOT NULL,              
    -- description or list of manuscripts that have this reading 
    witnesses       VARCHAR(255),               
    
    FOREIGN KEY (vu_id)
      REFERENCES NTTEXTComm_VUs (vu_id)
      ON DELETE CASCADE
    
) ENGINE=InnoDB CHARSET utf8;



-- Mapping of Entries to the VU's those entries describe
CREATE TABLE IF NOT EXISTS NTTEXTComm_EntryVUs (
    entry_id        BIGINT UNSIGNED NOT NULL,
    vu_id           BIGINT UNSIGNED NOT NULL,
    
    FOREIGN KEY (entry_id)
      REFERENCES NTTEXTComm_Entries (entry_id)
      ON DELETE CASCADE,
    FOREIGN KEY (vu_id)
      REFERENCES NTTEXTComm_VUs (vu_id)
      ON DELETE CASCADE
) ENGINE=InnoDB CHARSET utf8;

-- Mapping of variant readings to the manuscripts in which those readings are found
-- CREATE TABLE IF NOT EXISTS NTTEXTComm_RdgWitnesses (
    
    
-- ) ENGINE=InnoDB CHARSET utf8;