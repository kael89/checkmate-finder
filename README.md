# Chasemate

Back-end of https://chasemate.app/

Front-end repository: [chasemate-ui](https://github.com/kael89/chasemate-ui)

## Installation

### Run the server locally

Prerequisites:

- Maven

```
mvn clean package
java -jar target\chasemate-1.0-SNAPSHOT-jar-with-dependencies.jar
```

The server should now be running at http://localhost:8080

## API Documentation

- Base path: `/api`
- Output structure: JSON object of `{ data: any, error: string }` schema

### Type Definitions

| Name          | Type   | Info                                                                    | Examples                                                     | Comments                                                                                             |
| ------------- | ------ | ----------------------------------------------------------------------- | ------------------------------------------------------------ | ---------------------------------------------------------------------------------------------------- |
| Color         | Enum   | Values: `'black'`\|`'white'`                                            | `'black'`                                                    |                                                                                                      |
| ChessTree     | Object | Shape: `{[ [Position]: ChessTree[, [Position]: ChessTree[, ...]]] }`    | `{}`, `{ A1-A2: {} }`, `{ D2-D4: { D7-D6: {}, D7-D5: {} } }` |
| ChessTreeType | Enum   | Values: `'game'`\|`'forcedMate'`                                        | `'forcedMate'`                                               |                                                                                                      |
| Move          | String | Regex: `[A-H][1-8]-[A-H][1-8](=[B\|N\|Q\|R])?`                          | `'B2-E5'`,<br> `'D7-D8=Q'`                                   | The part after `=` indicates pawn promotion: `B` => Bishop, `N` => Knight, `Q` => Queen, `R` => Rook |
| Piece         | Object | Shape: `{ type: PieceType, color: Color, position: Position}`           | `{ type: 'pawn', color: 'white', positon: 'B3' }`            |                                                                                                      |
| PieceType     | Enum   | Values: `'bishop'`\|`'king'`\|`'knight'`\|`'queen'`\|`'rook'`\|`'pawn'` | `'bishop'`                                                   |                                                                                                      |
| Position      | String | Regex: `[A-H][1-8]`                                                     | `'B3'`                                                       |                                                                                                      |

### API List

#### getTree

| Url             | Method | Input                                 | Output (`data`) | Description                                                                                     |
| --------------- | ------ | ------------------------------------- | --------------- | ----------------------------------------------------------------------------------------------- |
| `/api/get-tree` | GET    | Url query parameters, see table below | `ChessTree`     | Returns a tree of chess moves, based on a specified board, starting player color and move depth |

##### Input parameters

| Name          | Type            | Required | Example                                                                                              | Description                                                                                                                                                                                                                  |
| ------------- | --------------- | -------- | ---------------------------------------------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| type          | `ChessTreeType` | Yes      | `game`                                                                                               | The type of chess tree to return:<br> - `game` returns all available moves for each player<br> - `forcedMate` returns starting player's moves that lead to forced mates in his favor, together with possible enemy responses |
| board         | `Piece[]`       | Yes      | `[{ type: 'pawn', color: 'white', position: 'E2' }, {type: 'pawn', color: 'black', position: 'E7'}]` | The starting board                                                                                                                                                                                                           |
| startingColor | Color           | Yes      | `black`                                                                                              | Determines which player plays first                                                                                                                                                                                          |
| depth         | Integer         | Yes      | `4`                                                                                                  | The number of moves for which the move tree will be created. Each player's move increases the depth by one                                                                                                                   |

## Built with

- [Java 8](https://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html)
- [Java Servlet](https://javaee.github.io/servlet-spec/)
- [Maven](https://maven.apache.org/)
- [JUnit 5](https://junit.org/junit5/)
- [Mockito](https://site.mockito.org/)
- [Jackson](https://github.com/FasterXML/jackson)
- [Apache Tomcat](https://tomcat.apache.org/)
- [AWS Lambda](https://docs.aws.amazon.com/lambda/index.html)

## Authors

**Kostas Karvounis** - [kael89](https://github.com/kael89)

## License

This project is licensed under the GNU General Public License v3.0
