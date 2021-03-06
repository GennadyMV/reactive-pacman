import GameService from "../GameService";
import {Config, Nickname} from "game-idl";
import {Single} from "rsocket-flowable";
import {IMeterRegistry} from "rsocket-rpc-metrics";

export default class GameServiceClientAdapter implements GameService {

    constructor(private readonly socket: SocketIOClient.Socket, meterRegistry: IMeterRegistry) {
        // this.service = new RSocketRPCServices.GameServiceClient(rSocket, undefined, meterRegistry);
    }

    start({ value }: Nickname.AsObject): Single<Config.AsObject> {
        const nicknameProto = new Nickname();

        nicknameProto.setValue(value);

        return new Single<Config>((downstream) => {
                let cancelled = false;

                downstream.onSubscribe(() => {
                    cancelled = true;
                });

                if (!cancelled) {
                    ((this.socket as any).binary(true) as SocketIOClient.Socket).emit("start", nicknameProto.serializeBinary(), (data: Buffer) =>{
                          downstream.onComplete(Config.deserializeBinary(data));
                    })
                }
            })
            .map((c: Config) => c.toObject());
    }
}