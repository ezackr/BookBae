import logo from './logo.png';
import React, {Component} from 'react';
import './App.css';
import axios from "axios";

/**
 * React prototype displaying how to use the Google Books API for BookBae.
 * Promises are handled using axios.
 * Plenty of tutorials and spec docs available for Google Books online.
 */
class App extends Component<> {
    toggleText = true;

    constructor(props) {
        super(props)
        this.state = {
            textValue: "Insert Book Title...",
            bookList: [],
            authorList: [],
            coverList: [],
        }
    }

    findBook(event) {
        event.preventDefault();

        axios.get("https://www.googleapis.com/books/v1/volumes?q=" + this.state.textValue)
            .then(data => {
                let titles = [];
                let authors = [];
                let covers = [];
                // Can replace with data.data.items.length for full list
                for (let i = 0; i < 5; i++) {
                    let item = data.data.items[i];
                    console.log(item);              // Displays information available in API.
                    // Add title:
                    if (item.volumeInfo.title !== undefined) {
                        titles.push(
                            <p>{item.volumeInfo.title}</p>
                        )
                    } else {
                        continue;
                    }
                    // Add author:
                    if (item.volumeInfo.authors !== undefined) {
                        authors.push(
                            <p>{item.volumeInfo.authors[0]}</p>
                        )
                    } else {
                        authors.push(
                            <p>Unknown</p>
                        )
                    }
                    // Add cover:
                    if (item.volumeInfo.imageLinks !== undefined) {
                        covers.push(
                            <img src={item.volumeInfo.imageLinks.thumbnail} alt="cover photo"/>
                        )
                    } else {
                        covers.push(
                            <p>No Image Available</p>
                        )
                    }
                }
                this.setState({
                    bookList: titles,
                    authorList: authors,
                    coverList: covers,
                });
            });
    }

    updateText(event) {
        this.setState({
            textValue: event.target.value,
        })
    }

    clear(event) {
        if (this.toggleText) {
            this.setState({
                textValue: "",
            })
            this.toggleText = false;
        }
    }

    compileData() {
        let data = [];
        for (let i = 0; i < this.state.bookList.length; i++) {
            data.push([this.state.bookList[i], this.state.coverList[i], this.state.authorList[i]]);
        }
        return data;
    }


  render() {
    let data = this.compileData();

    return (
        <div className="App">
            <header className="App-header">
            <h2>BookBae Search Prototype:</h2>
            <img src={logo} className="App-logo" alt="logo"/>
            <br/>
            <table>
                <tbody>
                    <tr>
                        <td>
                        <textarea
                            id="search-bar"
                            cols="30"
                            rows="1"
                            value={this.state.textValue}
                            onClick={(event) => this.clear(event)}
                            onChange={(event) => this.updateText(event)}
                        />
                        </td>
                        <td>
                        <button
                            style={{backgroundColor: "cornflowerblue"}}
                            onClick={(event) => this.findBook(event)}
                        >Enter</button>
                        </td>
                        <td>
                        <button
                            onClick={
                                (event) => {
                                    this.toggleText = true;
                                    this.setState({
                                        textValue: "Insert Book Title...",
                                        bookList: [],
                                    });
                                }}
                        >Clear</button>
                        </td>
                    </tr>
                </tbody>
            </table>
            <table className="book-list">
                <tbody>
                {data.map((entry) => (
                    <tr>
                        <td className="title">{entry[0]}</td>
                        <td className="cover">{entry[1]}</td>
                        <td className="author">{entry[2]}</td>
                    </tr>
                ))}
                </tbody>
            </table>
            </header>
        </div>
    );
  }
}

export default App;
