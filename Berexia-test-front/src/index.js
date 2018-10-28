import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import registerServiceWorker from './registerServiceWorker';
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
this.engine = new DiagramEngine();
this.engine.registerNodeFactory(new DefaultNodeFactory());
this.engine.registerLinkFactory(new DefaultLinkFactory());
this.model = new DiagramModel()
this.tools={engine: this.engine,model:this.model}
export default this.tools;

ReactDOM.render(<App />, document.getElementById('root'));
registerServiceWorker();
