import React, {Component} from 'react';
import {BrowserRouter as Router, Route, Switch} from "react-router-dom";
import Main from './views/Main'
import Login from "./views/Login";

class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route exact path="/login" component={Login}></Route>
                    <Route path="/" component={Main}></Route>
                </Switch>
            </Router>
        );
    }
}

export default App;