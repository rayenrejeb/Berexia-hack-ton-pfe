import React, { Component } from 'react';
import { HashRouter, Route, Switch } from 'react-router-dom';
import {
	DiagramWidget,
	DiagramEngine,
	DefaultNodeFactory,
	DefaultLinkFactory,
	DefaultNodeModel,
	DefaultPortModel,
	LinkModel,
  DiagramModel
} from 'storm-react-diagrams';
import Graphe from './components/Graphe';
import Menu from './components/Menu';
import './App.css';


class App extends Component {
  componentWillMount() {
	
    }
    render() {
    return (
      <div>
      <Graphe model={this.model} engine={this.engine} ></Graphe>
      </div>
    );
  }
}
export default App;
