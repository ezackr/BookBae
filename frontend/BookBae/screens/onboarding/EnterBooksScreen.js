import React from 'react';
import {
  SafeAreaView,
  View,
  StyleSheet,
  Text,
  TextInput,
  Pressable,
  FlatList,
  Image,
} from 'react-native';
import axios from 'axios';

const EnterBooksScreen = ({route, navigation}) => {
  const [text, setText] = React.useState(null);
  const [bookList, setBookList] = React.useState([]);
  const [searchResults, setSearchResults] = React.useState([]);

  // Pass the ids of the selected books on to the next onboarding screen
  const onPress = () => {
    console.log('leave books');
    const coverList = bookList.map(book => book.volumeInfo.imageLinks.smallThumbnail);
    navigation.navigate('EnterPhotoScreen', {
      ...route.params,
      books: coverList,
    });
  };

  // Adds a book to the user's list
  const addNewBook = book => {
    setBookList(prevState => {
      prevState.push(book);
      return [...prevState];
    });
  };

  // Displays search results;
  const onSearch = () => {
    axios
      .get('https://www.googleapis.com/books/v1/volumes?q=' + text)
      .then(response => {
        console.log(response.data.items[0].volumeInfo.imageLinks);
        setSearchResults(response.data.items);
      });
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Start Typing a Book Title:</Text>

      {/* search bar */}
      <TextInput
        style={styles.input}
        placeholder="Insert Book Title"
        onChangeText={setText}
        onSubmitEditing={onSearch}
        defaultValue={text}
      />

      {/* search button */}
      <Pressable style={styles.button} onPress={onSearch}>
        <Text style={styles.buttonText}>Search</Text>
      </Pressable>

      {/* search results */}
      <View style={styles.bookList}>
        <FlatList
          data={searchResults}
          extraData={searchResults}
          renderItem={({item}) => (
            <BookListItem book={item} onClick={() => addNewBook(item)} />
          )}
        />
      </View>

      <Text style={styles.title}>My Bookshelf:</Text>
      {/* selected books */}
      <View style={styles.bookList}>
        <FlatList
          data={bookList}
          extraData={bookList}
          renderItem={({item}) => <BookListItem book={item} />}
        />
      </View>

      {/* next page button */}
      <Pressable style={styles.button} onPress={onPress}>
        <Text style={styles.buttonText}>Next</Text>
      </Pressable>
    </SafeAreaView>
  );
};

const BookListItem = ({book, onClick}) => {
  return (
    <Pressable onPress={onClick}>
      <View style={{flexDirection: 'row'}}>
        <Image
          style={styles.image}
          source={{uri: book.volumeInfo.imageLinks.smallThumbnail}}
        />
        <Text style={styles.book}> {book.volumeInfo.title} </Text>
      </View>
    </Pressable>
  );
};

const styles = StyleSheet.create({
  title: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
  },
  bookList: {
    flex: 1,
    padding: 10,
  },
  book: {
    color: 'black',
    fontSize: 20,
  },
  input: {
    height: 40,
    width: '70%',
    margin: 1,
    borderWidth: 1,
    padding: 10,
  },
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  imageContainer: {
    paddingTop: 5,
    paddingBottom: 5,
  },
  image: {
    width: 40,
    height: 60,
  },
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: '#BD2A2A',
    marginTop: 5,
    marginBottom: 5,
  },
  buttonText: {
    fontSize: 16,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'white',
  },
});

export default EnterBooksScreen;
