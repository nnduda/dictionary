# English learning app with in-built dictionary

## Used technologies

- Java 17 (features mostly from 8)
- Spring Boot - for stand-alone Spring functionality
- H2 Database - with possible migration to MySQL
- Thymeleaf - as template engine
- Lombok - for annotation generated code
-

## Functionalities

### Dictionary

- English -> Polish translation (and possibly the other way around)
- Words' definitions (English) & examples
- Words' audio (optional)
- Adding new words, translations, examples, synonyms to the database
- Editing existing records (added by user) and leaving notes to basic ones
    - select note from notes where external_table_id=? and external_table_name = 'word_extras';
- Voting up/down for user entries (by other users)
- List of changes made by users, votes received, rank
-

### Quiz making

- Quiz from previously searched words
- Quiz from random words
- Quiz from chosen words
- Quiz in form of a,b,c,d answers
- Creating link to quiz - public and for registered users
- Quiz scores after filling, statistics for user and global (ranks)
- Quiz list - both private (for creator and people with link) and public (visible to everyone)
-

### Social network

- Registering users
- Edits visible to all users (with filters for minimum votes required to show record - avoid spam)
- List of quizes, ranks
- Sharing link to quiz
- Administrator account with access to all functionalities
-

## Used data

- English dictionary - https://dictionaryapi.dev/
- English <-> Polish dictionary - https://github.com/freedict/fd-dictionaries

#### Meaning of the tags

    <entry>
        <form>
            <orth></orth>
            <pron></pron>
        <gramGrp>
            <pos></pos>
        </gramGrp>
        <sense>
            <cit>
                <quote></quote>
            </cit>
        </sense>
    </form>
    </entry>

      <form xml:lang="en">
         <orth>aardvark</orth>  --> word
         <pron>ˈaːdvaːk</pron> --> pronunciation
      </form>
      <gramGrp>
         <pos>N</pos> -->  partOfSpeech
      </gramGrp>
        <xr>
            <ref>American Broadcasting Company</ref> --> phrase?
     </xr>
                <cit type="trans"> -->  type 
                  <quote>mrównik</quote> --> quote
    </cit>

